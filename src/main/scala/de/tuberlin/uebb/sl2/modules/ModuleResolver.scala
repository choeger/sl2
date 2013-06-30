package de.tuberlin.uebb.sl2.modules

import java.io.File

/**
 * A ModuleResolver is able to find and load the modules a program
 * references.
 */
trait ModuleResolver {
  this: Syntax with Context with Errors with Configs =>

  sealed abstract class ResolvedImport
  
  case class ResolvedQualifiedImport(
      path: String,
      name: String,
      file: File,
      signature: Program,
      ast: QualifiedImport)
    extends ResolvedImport
    
  case class ResolvedExternImport(
      file: File,
      ast: ExternImport)
    extends ResolvedImport
    
  def inferDependencies(program: AST, config: Config): Either[Error, List[ResolvedImport]]

}