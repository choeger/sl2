package de.tuberlin.uebb.sl2.modules

import de.tuberlin.uebb.sl2.modules._
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import scala.io.Source
import scalax.file.FileSystem
import scalax.file.Path

/**
 * Sort the nodes in a given set of edges topologically.
 */
trait ModuleLinearization
	extends Object
	with Configs
	with Errors
	with ModuleResolver
	with Syntax {
  	  
  abstract class AbstractFile {
    def filename():String
    def parent():File
    def path():String
    def canRead():Boolean
    def lastModified():Long
    def contents():String
  }
  
  class PathedFile(path: Path) extends AbstractFile {
    def filename() = {
      path.name
    }
    
    def parent() = {
      if(path.parent != null)
    	  new File(path.parent.toString)
      else
    	  new File(".")
    }
    
    def path() = {
      path.toString
    }
    
    def canRead() = {
      path.canRead
    }
    
    def lastModified() = {
      path.lastModified
    }
    
    def contents() = {
      path.lines(includeTerminator = true).mkString("\n")
    }
  }
  
  class ErrorFile(url: URL) extends AbstractFile {
    def filename = { "Error" }
    def parent = { new File(".") }
    def path = { throw new UnsupportedOperationException() }
    def canRead() = { false }
    def lastModified() = { 0 }
    def contents() = { throw new UnsupportedOperationException() } 
  }
  
  /**
   * A file inside a .jar file 
   */
  class BottledFile(url: URL) extends AbstractFile {
    
    def filename() = {
      println("BottledFile.name()="+url.getFile)
      url.getFile()
    }
    
    def parent() = {
      new File(".")
    }
    
    def path() = {
      url.toString
    }
    
    def canRead() = {
      /*
       * A bottled file can always be read. Otherwise, its location wouldn't have been found.
       */
      true
    }
    
    def lastModified() = {
      /*
       * The modification date for a bottled does not matter, because all files
       * (source, js and signature) in a JAR file have the same date and cannot
       * be modified.
       */
      0
    }
    
    def contents() = {
      Source.fromURL(url).getLines.mkString("\n")
    }
  }
  
  sealed class Module {
    var name: String = ""
    var source: AbstractFile = null
    var signature: AbstractFile = null
    var js: AbstractFile = null
    var compile: Boolean = false
    
    def this(name: String, config: Config) = {
      this()
      this.name = name
      if(name.startsWith("std/")) {
          // load std/ library from resources directory
          this.name = name.replace("std/", "")

          val sourceURL = getClass().getResource("/lib/"+this.name+".sl")
          println("stdSource="+sourceURL)
          if(sourceURL == null)
            throw new IOException("Could not find source of standard library: "+quote("/lib/"+name))
    	  println("stdSource="+source)
          this.source = createFile(sourceURL)
          this.signature = createFile(new URL(sourceURL.toString+".signature"))
          this.js = createFile(new URL(sourceURL.toString+".js"))
      } else {
          // load ordinary files relative to source- and classpath
    	  this.source = createFile(new URL(config.sourcepath.toURI.toURL, name+".sl"))
    	  println("ordinarySource="+source)
          this.signature = createFile(new URL(config.classpath.toURI.toURL, name+".sl.signature"))
          this.js = createFile(new URL(config.classpath.toURI.toURL, name+".sl.js"))
      }
    }
    
    /*
     * Returns null in case of error 
     */
    private def createFile(url: URL):AbstractFile = {
      if(url.toString.startsWith("jar:")) {
    	  new BottledFile(url)
      } else {
          val option = Path(url.toURI)
          if(option.isEmpty)
        	  new ErrorFile(url)
          else
        	  new PathedFile(option.get)
      }
    }
    
    override def equals(obj: Any) = {
      (obj != null &&
       obj.isInstanceOf[Module] &&
       obj.asInstanceOf[Module].name == this.name)
    }
    
    override def hashCode() = { name.hashCode }
    
    override def toString() = {
      "(Module "+quote(name)+" (compile="+compile+"))\n"
    }
  }
  
	/**
	 * Sorts the modules in the map from modules to sets of their respective
	 * required modules topologically and returns the sorted sequence of modules.
	 */
	def topoSort(predecessors: scala.collection.Map[Module, Set[Module]]):
		Either[Error,Iterable[Module]] = {
	  topoSort(predecessors, Seq())
	}
  
	/**
	 * Sorts the modules in the map from modules to sets of required modules topologically.
	 * 
	 * Recursively sort topologically: add the modules to the set of done modules 
	 * that have no predecessors, remove them from the map of predecessors and
	 * recurse until no modules are left. Return an error, if modules are left.
	 */
	def topoSort(predecessors: scala.collection.Map[Module, Set[Module]],
	             done: Iterable[Module]): Either[Error,Iterable[Module]] = {
		val (hasNoPredecessors, hasPredecessors) = predecessors.partition { _._2.isEmpty }
		if (hasNoPredecessors.isEmpty) {
			if (hasPredecessors.isEmpty) {
			  Right(done)
			} else {
			  println("Circular dependencies")
			  Left(CircularDependencyError("Circular dependency between modules "+
			      (for(key <- hasPredecessors.keys) yield key.source.toString).mkString(", ")))
			}
		} else {
			val found = hasNoPredecessors.map { _._1 }
			topoSort(hasPredecessors.mapValues { _ -- found }, done ++ found)
		}
	}
}
