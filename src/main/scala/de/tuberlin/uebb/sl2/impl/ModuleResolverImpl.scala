package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._
import java.io.File
import scala.io.Source

trait ModuleResolverImpl extends ModuleResolver {
  this: Syntax with Errors with Configs with SignatureSerializer =>

  case class ImportError(what: String, where: Attribute) extends Error {
    override def toString = where.toString + ": " + what + "\n"
  }

  def inferDependencies(program: AST, config: Config) : Either[Error, List[ResolvedImport]] = program match {
    case Program(imports, _, _, _, _, attribute) =>
      checkImports(imports) match {
        case Left(err) => return Left(err)
        case _ =>
      }
      
      val preludeImp = if (config.mainName != "prelude.sl")
         UnqualifiedImport("std/prelude") :: imports 
      else
         imports
      errorMap(preludeImp, resolveImport(config))
    case _ => throw new RuntimeException("")
  }
  
  def checkImports(imports : List[Import]) : Either[Error, Unit] = {
    for (
      _ <- checkPathSyntax(imports).right;
      _ <- checkUniquePath(imports).right;
      _ <- checkUniqueIde(imports).right
    ) yield ()
  }
  
  private def checkPathSyntax(imports : List[Import]) : Either[Error, Unit] = {
    if (imports.isEmpty)
      return Right()
    
    /** Checks the syntax of the path
     *
     * The underscore indicates terminal symbols.
     * Within '[' ']' only terminal symbols are enumerated.
     *
     * The syntax is defined as follows:
     *
     *   path   ::= _/?(dir _/) module
     *   dir    ::= char+
     *   module ::= char+
     *   char   ::= [:lower:] | [:digit:] | [-_]
     */
    def validatePath(imp : Import) : Either[Error, Unit] = {
      if (imp.path.equals("prelude") || imp.path.equals("std/prelude"))
    	return Left(ImportError("Prelude must not be imported explicitly.", imp.attribute))
      else if (imp.path.matches("/?([a-z0-9-_]+/)*[a-z0-9-_]+"))
        return Right()
      else
        return Left(InvalidPathError(imp.path, imp.attribute))
    }
    
    imports.map(validatePath).reduce(collectErrors)
  }
  
  private def checkUniquePath(imports : List[Import]) : Either[Error, Unit] = {
    if (imports.isEmpty)
      return Right()
    
    def checkUniqueness(imp : Import) : Either[Error, Unit] = {
      if (imports.count(_.path == imp.path) != 1)
        return Left(DuplicatePathError(imp.path, imp.attribute))
      else 
        return Right()
    }
    
    imports.map(checkUniqueness).reduce(collectErrors)
  }
  
  private def checkUniqueIde(imports : List[Import]) : Either[Error, Unit] = {
    val qualified = imports.filter(_.isInstanceOf[QualifiedImport]).map(_.asInstanceOf[QualifiedImport])

    if (qualified.isEmpty)
      return Right()
    
    def checkUniqueness(imp : QualifiedImport) : Either[Error, Unit] = {
      if (qualified.count(_.name == imp.name) == 1)
        return Right()
      else
        return Left(DuplicateModuleError(imp.name, imp.attribute))
    }
    
    if(!qualified.isEmpty)
    	qualified.map(checkUniqueness).reduce(collectErrors)
    else
    	Right(Unit) // avoids UnsupportedOperationException: empty.reduce
  }
  
  /**
   * Returns the names of modules imported in the given AST. This does not include
   * the implicitly imported prelude and imported externs, because both do not
   * need to be compiled.
   */
  def resolveDependencies(program: AST, config: Config) : Either[Error, Set[String]] = program match {
    case Program(imports, _, _, _, _, attribute) =>
      checkImports(imports) match {
        case Left(err) => return Left(err)
        case _ =>
      }
      var paths = Set[String]()
      // ignore extern imports
      for(resolvedImports <- errorMap(imports.filter( x =>
        	(x.isInstanceOf[UnqualifiedImport]) ||
        	(x.isInstanceOf[QualifiedImport])), collectImport(config)).right;
          resolvedImport  <- resolvedImports) {
    	  paths = paths + resolvedImport
    	  }
      Right(paths)
    case _ => throw new RuntimeException("")
  }

  def collectImport(config: Config)(imp: Import): Either[Error, String] = { Right(imp.path) }
  
  def resolveImport(config: Config)(imp: Import): Either[Error, ResolvedImport] = imp match {
    case ui @ UnqualifiedImport(path, attr) => 
      val stdPrefix = "std/" // TODO: Do this less rigidly
      if (stdPrefix != path.substring(0, stdPrefix.length))
        return Left(GenericError("unqualified import " + path + " doesn't start with " + stdPrefix))
      for (
        file <- findImportResource(imp.path.substring(stdPrefix.length) + ".sl.signature", config, attr).right;
        jsFile <- findImportResource(imp.path.substring(stdPrefix.length) + ".sl.js", config, attr).right;
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
      ) yield ResolvedExternImport(path, file, ei)
  }

  def findImportResource(path: String, config: Config, attr: Attribute): Either[Error, File] = {
    val url = getClass().getResource("/lib/");
    val file = new File(new File(url.toURI()), path) // TODO: wirft NPE, wenn url==null
    if(url == null || file.canRead()) { // TODO: was ist denn das für eine Bedingung?
        // TODO: Will need to modify all uses of ResolvedUnqualifiedImport, for they may stem from JAR file or from path
    	Left(ImportError("Could not find resource " + quote("/lib/"+path), attr))
    } else {
	    val files = List(file,
	        new File(config.classpath, path), // the last two paths are necessary to compile the std. libraries
	        new File(config.destination, path))
	    files.find(_.canRead()).toRight(
	      ImportError("Could not find resource " + quote(path)+ " at " + files.map(_.getCanonicalPath()).mkString("\n\t\t\t\tor "), attr))
    }
  }
  
  def findImport(config: Config, path: String, attr: Attribute): Either[Error, File] = {
    val stdPrefix = "std/" // TODO: Do this less rigidly
    if (stdPrefix == path.substring(0, stdPrefix.length)) {
      findImportResource(path.substring(stdPrefix.length), config, attr)
    } else {
      val files = List(new File(config.classpath, path),
        new File(config.destination, path),
        new File(config.mainParent, path),
        new File(path))
      files.find(_.canRead()).toRight(
        ImportError("Could not find " + quote(path) + " at " + files.map(_.getCanonicalPath()).mkString("\n\t\t\t\tor "), attr))
    }
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
