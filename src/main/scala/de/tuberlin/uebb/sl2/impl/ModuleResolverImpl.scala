package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._
import java.io.File
import scala.io.Source

trait ModuleResolverImpl extends ModuleResolver {
  this: Syntax with Errors with Configs with SignatureSerializer =>

  case class ImportError(what: String, where: Attribute) extends Error

  def inferDependencies(program: AST, config: Config) = program match {
    case Program(imports, _, _, _, _, attribute) =>
      //TODO: really deal with transitive imports and the like...
      errorMap(imports, resolveImport(config))
    case _ => throw new RuntimeException("")
  }

  def resolveImport(config: Config)(imp: Import): Either[Error, ResolvedImport] = imp match {
    case qi @ QualifiedImport(path, name, attr) =>
      for (
        file <- findImport(config, imp.path + ".sl.signature", attr).right;
        signature <- importSignature(file).right
      ) yield ResolvedQualifiedImport(name, path, file, signature, qi)
    case ei @ ExternImport(path, attr) =>
      for (
        file <- findImport(config, imp.path + ".sl.js", attr).right
      ) yield ResolvedExternImport(file, ei)
  }

  def findImport(config: Config, path: String, attr: Attribute): Either[Error, File] = {
    //TODO: look at places according to some defined classpath hierarchie etc.
    val files = List(new File(config.classpath, path), new File(path))
    files.find(_.canRead()).toRight(
      ImportError("Could not find " + quote(path) + " at " + files.map(_.getCanonicalPath()), attr))
  }

  def importSignature(file: File): Either[Error, Program] = {
    val sigJson = Source.fromFile(file).getLines.mkString("\n")
    val signature = deserialize(sigJson)
    if (null == signature) {
      Left(ImportError("Failed to load signature " + file, EmptyAttribute))
    } else {
      Right(signature.asInstanceOf[Program])
    }
  }
}