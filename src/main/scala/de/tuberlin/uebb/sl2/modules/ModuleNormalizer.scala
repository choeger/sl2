package de.tuberlin.uebb.sl2.modules

trait ModuleNormalizer {
	this : Syntax =>
	
	def normalizeModules(imports : List[(Import, AST)]) : AST

}
