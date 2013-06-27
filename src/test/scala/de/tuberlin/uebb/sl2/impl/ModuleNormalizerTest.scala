package de.tuberlin.uebb.sl2.tests.impl

import de.tuberlin.uebb.sl2.tests.specs.ModuleNormalizerSpec
import de.tuberlin.uebb.sl2.impl._

class ModuleNormalizerTest extends ModuleNormalizerSpec with ModuleNormalizerImpl with ParboiledParser with SignatureJsonSerializer {
	
	def testedImplementationName = "Substitution module normalizer"

}
