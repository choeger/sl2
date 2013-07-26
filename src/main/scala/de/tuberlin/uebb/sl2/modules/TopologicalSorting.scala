package de.tuberlin.uebb.sl2.modules

import de.tuberlin.uebb.sl2.modules._
import java.io.File

/**
 * Sort the nodes in a given set of edges topologically.
 */
trait TopologicalSorting
	extends Object
	with Configs
	with Errors
	with ModuleResolver
	with Syntax {
  	  
  sealed class Module {
    var name: String = ""
    var sourceFile: File = null
    var signatureFile: File = null
    var jsFile: File = null
    var compile: Boolean = false
    
    def this(name: String, config: Config) = {
      this()
      this.name = name
      if(name.startsWith("std/")) {
          // load std/ library from resources directory
          this.name = name.replace("std/", "")
          this.sourceFile = fromRight(findImportResource(this.name+".sl", EmptyAttribute))
          this.signatureFile = fromRight(findImportResource(this.name+".sl.signature", EmptyAttribute))
          this.jsFile = fromRight(findImportResource(this.name+".sl.js", EmptyAttribute))
      } else {
          // load ordinary files relative to source- and classpath
          this.sourceFile = new File(config.sourcepath, name+".sl")
          this.signatureFile = new File(config.classpath, name+".sl.signature")
          this.jsFile = new File(config.classpath, name+".sl.js")
      }
    }

    def fromRight(ei: Either[Error, File]):File = ei match {
      case Right(f) => return f
      case Left(err) => throw err
    }

    override def toString() = {
      "(Module "+quote(name)+" (compile="+compile+")"+/*
      	" sourceFile="+quote(sourceFile.getCanonicalPath())+
      	" signatureFile="+quote(signatureFile.getCanonicalPath())+
      	" jsFile="+quote(jsFile.getCanonicalPath())+*/")\n"
    }
  }
  
	/**
	 * Sorts the nodes in the map from nodes to sets of their respective
	 * required nodes topologically and returns the sorted sequence of nodes.
	 */
	def topoSort(predecessors: Map[Module, Set[Module]]): Either[Error,Iterable[Module]] = {
	  topoSort(predecessors, Seq())
	}
  
	/**
	 * Sorts the nodes in the map from nodes to sets of required nodes topologically.
	 * 
	 * Recursively sort topologically: add the nodes to the set of done nodes
	 * that have no predecessors, remove them from the map of predecessors and
	 * recurse until no nodes are left.
	 */
	def topoSort(predecessors: Map[Module, Set[Module]],
	             done: Iterable[Module]): Either[Error,Iterable[Module]] = {
		val (hasNoPredecessors, hasPredecessors) = predecessors.partition { _._2.isEmpty }
		if (hasNoPredecessors.isEmpty) {
			if (hasPredecessors.isEmpty)
			  Right(done)
			else
			  Left(CircularDependencyError("Circular dependency between modules "+
			      (for(key <- hasPredecessors.keys) yield key.sourceFile.getCanonicalPath).mkString(", ")))
		} else {
			val found = hasNoPredecessors.map { _._1 }
			topoSort(hasPredecessors.mapValues { _ -- found }, done ++ found)
		}
	}
}
