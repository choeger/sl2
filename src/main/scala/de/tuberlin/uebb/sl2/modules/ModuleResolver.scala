package de.tuberlin.uebb.sl2.modules

import java.io.File

/**
 * A ModuleResolver is able to find and load the modules a program
 * references.
 */
trait ModuleResolver {
  this: Syntax with Errors with Configs =>

  sealed abstract class ResolvedImport(
      val path:String,
      val file:File,
      val ast:Import)
  
  sealed abstract class ResolvedNamedImport(
      val name:String,
      override val path:String,
      override val file:File,
      val jsFile:File,
      val signature:Program,
      override val ast:Import) extends ResolvedImport(path, file, ast)
      
  case class ResolvedUnqualifiedImport(
      override val path: String,
      override val file: File,
      override val jsFile: File,
      override val signature: Program,
      override val ast: UnqualifiedImport) extends ResolvedNamedImport(
          "$$"+path.replace('/', '$'), path, file, jsFile, signature, ast)
  
  case class ResolvedQualifiedImport(
      override val name: ModuleVar,
      override val path: String,
      override val file: File,
      override val jsFile: File,
      override val signature: Program,
      override val ast: QualifiedImport)
    extends ResolvedNamedImport(name, path, file, jsFile, signature, ast)
    
  case class ResolvedExternImport(
      override val path: String,
      override val file: File,
      override val ast: ExternImport)
    extends ResolvedImport(path, file, ast)
    
  def inferDependencies(program: AST, config: Config): Either[Error, List[ResolvedImport]]
  def resolveDependencies(program: AST, config: Config): Either[Error, Set[String]]

}