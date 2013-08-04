package de.tuberlin.uebb.sl2.modules

import de.tuberlin.uebb.sl2.modules._
import java.io.File
import java.io.IOException
import java.net.URL

/**
 * Sort the nodes in a given set of edges topologically.
 */

trait ModuleLinearization
  extends Object
  with Configs
  with Errors
  with ModuleResolver
  with Syntax {

  /**
   * module needed in compilation process.
   */
  sealed case class Module(
    val name: String = "",
    val sourceFile: File = null,
    val signatureFile: File = null,
    val jsFile: File = null,
    val compile: Boolean = false) {

    override def equals(obj: Any) = {
      (obj != null &&
        obj.isInstanceOf[Module] &&
        obj.asInstanceOf[Module].name == this.name)
    }

    override def hashCode() = { name.hashCode }

    override def toString() = {
      "(Module " + quote(name) + " (compile=" + compile + "))\n"
    }
  }

  /**
   * creates a module compilation unit object from a file name
   * (can either be from /std)
   */
  def moduleFromName(name: String, config: Config): Module = {
    if (name.startsWith("std/")) {
      // load std/ library from resources directory
      val nameEnd = name.replace("std/", "")
      val stdSource = getClass().getResource("/lib/" + nameEnd + ".sl")
      if (stdSource == null)
        throw new IOException("Could not find source of standard library: "
          + quote("/lib/" + name))
      Module(nameEnd,
        new File(stdSource.toURI()),
        new File(new URL(stdSource.toString + ".signature").toURI()),
        new File(new URL(stdSource.toString + ".js").toURI()))
    } else {
      // load ordinary files relative to source- and classpath
      Module(name,
        new File(config.sourcepath, name + ".sl"),
        new File(config.classpath, name + ".sl.signature"),
        new File(config.classpath, name + ".sl.js"))
    }
  }

  /**
   * Sorts the modules in the map from modules to sets of their respective
   * required modules topologically and returns the sorted sequence of modules.
   */
  def topoSort(predecessors: scala.collection.Map[Module, Set[Module]]): Either[Error, Iterable[Module]] = {
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
    done: Iterable[Module]): Either[Error, Iterable[Module]] = {
    val (hasNoPredecessors, hasPredecessors) = predecessors.partition { _._2.isEmpty }
    if (hasNoPredecessors.isEmpty) {
      if (hasPredecessors.isEmpty) {
        Right(done)
      } else {
        println("Circular dependencies")
        Left(CircularDependencyError("Circular dependency between modules " +
          (for (key <- hasPredecessors.keys) yield key.sourceFile.getCanonicalPath).mkString(", ")))
      }
    } else {
      val found = hasNoPredecessors.map { _._1 }
      topoSort(hasPredecessors.mapValues { _ -- found }, done ++ found)
    }
  }
}
