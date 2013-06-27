package de.tuberlin.uebb.sl2.modules

trait ModuleImporter {
	this : Syntax =>
	
	def importSignatures(imports : List[(Import, AST)]) : AST

}
