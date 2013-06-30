package de.tuberlin.uebb.sl2.modules

trait ModuleNormalizer {
  this : Syntax with ModuleResolver =>
  
  def normalizeModules(imports : List[ResolvedImport]) : List[ResolvedImport]

}
