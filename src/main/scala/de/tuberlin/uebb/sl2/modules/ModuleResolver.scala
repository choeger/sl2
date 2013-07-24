package de.tuberlin.uebb.sl2.modules

import java.io.File

/**
 * A ModuleResolver is able to find and load the modules a program
 * references.
 */
trait ModuleResolver {
  this: Syntax with Errors with Configs =>

  sealed abstract class ResolvedImport(
      file:File,
      ast:Import)
  
  sealed abstract class ResolvedModuleImport(
      val name:String,
      val path:String,
      val file:File,
      val jsFile:File,
      val signature:Program,
      val ast:Import) extends ResolvedImport(file, ast)
      
  case class ResolvedUnqualifiedImport(
      override val path: String,
      override val file: File,
      override val jsFile: File,
      override val signature: Program,
      override val ast: UnqualifiedImport) extends ResolvedModuleImport(
          "$$"+path.replace('/', '$'), path, file, jsFile, signature, ast)
  
  case class ResolvedQualifiedImport(
      override val name: ModuleVar,
      override val path: String,
      override val file: File,
      override val jsFile: File,
      override val signature: Program,
      override val ast: QualifiedImport)
    extends ResolvedModuleImport(name, path, file, jsFile, signature, ast)
    
  case class ResolvedExternImport(
      file: File,
      ast: ExternImport)
    extends ResolvedImport(file, ast)
    
  def inferDependencies(program: AST, config: Config): Either[Error, List[ResolvedImport]]
  
  def checkImports(imports : List[Import]) : Either[Error, Unit]

}