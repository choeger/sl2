package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._
import java.io.File
import scala.io.Source

trait ModuleResolverImpl extends ModuleResolver {
  this: Syntax with Errors with Configs with SignatureSerializer =>

  case class ImportError(what: String, where: Attribute) extends Error

  def inferDependencies(program: AST, config: Config) : Either[Error, List[ResolvedImport]] = program match {
    case Program(imports, _, _, _, _, attribute) =>
      checkImports(imports) match {
        case Left(err) => return Left(err)
        case _ =>
      }
      
      //TODO: really deal with transitive imports and the like...
      val preludeImp = if (config.mainUnit.getName() != "prelude.sl")
        UnqualifiedImport("prelude") :: imports else imports
      errorMap(preludeImp, resolveImport(config))
    case _ => throw new RuntimeException("")
  }
  
  private def checkImports(imports : List[Import]) : Either[Error, Unit] = {
    for (
      _ <- checkUniquePath(imports).right;
      _ <- checkUniqueIde(imports).right
    ) yield ()
  }
  
  private def checkUniquePath(imports : List[Import]) : Either[Error, Unit] = {
    val duplicates = imports.filter {imp => imports.count(_.path == imp.path) > 1}
    
    if (duplicates.isEmpty)
      return Right()
    else
      return Left(DuplicatePathError(duplicates))
  }
  
  private def checkUniqueIde(imports : List[Import]) : Either[Error, Unit] = {
    Right()
  }

  def resolveImport(config: Config)(imp: Import): Either[Error, ResolvedImport] = imp match {
    case ui @ UnqualifiedImport(path, attr) => 
      for (
        file <- findImportResource(imp.path + ".sl.signature", attr).right;
        jsFile <- findImportResource(imp.path + ".sl.js", attr).right;
        signature <- importSignature(file).right
      ) yield ResolvedUnqualifiedImport(path, file, jsFile, signature, ui)
    case qi @ QualifiedImport(path, name, attr) =>
      for (
        file <- findImport(config, imp.path + ".sl.signature", attr).right;
        jsFile <- findImport(config, imp.path + ".sl.js", attr).right;
        signature <- importSignature(file).right
      ) yield ResolvedQualifiedImport(name, path, file, jsFile, signature, qi)
    case ei @ ExternImport(path, attr) =>
      for (
        file <- findImport(config, imp.path + ".js", attr).right
      ) yield ResolvedExternImport(file, ei)
  }

  def findImportResource(path: String, attr: Attribute): Either[Error, File] = {
    val files = List(new File(getClass().getResource("/lib/"+path).toURI()))
    files.find(_.canRead()).toRight(
      ImportError("Could not find resource " + quote(path)+ " at " +files.map(_.getCanonicalPath()), attr))
  }
  
  def findImport(config: Config, path: String, attr: Attribute): Either[Error, File] = {
    val files = List(new File(config.classpath, path),
        new File(config.mainUnit.getParentFile(), path),
        new File(path))
    files.find(_.canRead()).toRight(
      ImportError("Could not find " + quote(path) + " at " + files.map(_.getCanonicalPath()), attr))
  }

  def importSignature(file: File): Either[Error, Program] = {
    val source = scala.io.Source.fromFile(file.getCanonicalPath())
    val json = source.mkString
    source.close()
    val signature = deserialize(json, FileLocation(file.getName(), null, null))
    if (null == signature) {
      Left(ImportError("Failed to load signature " + file, EmptyAttribute))
    } else {
      Right(signature.asInstanceOf[Program])
    }
  }
}