package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._

trait ModuleNormalizerImpl extends ModuleNormalizer with Syntax {

	def normalizeModules(imports : List[(Import, AST)]) : AST =
		Program(List(), Map(), Map(), Map(), List())

}
