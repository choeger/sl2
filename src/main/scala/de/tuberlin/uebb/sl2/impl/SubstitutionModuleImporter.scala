package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules.{ ModuleImporter, Syntax }

trait SubstitutionModuleImporter extends ModuleImporter with Syntax {

	def importSignatures(imports : List[(Import, AST)]) : AST =
		Program(List(), Map(), Map(), List())

}
