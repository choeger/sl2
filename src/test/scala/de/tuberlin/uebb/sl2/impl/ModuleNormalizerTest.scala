package de.tuberlin.uebb.sl2.tests.impl

import de.tuberlin.uebb.sl2.modules._
import de.tuberlin.uebb.sl2.impl._
import de.tuberlin.uebb.sl2.tests.specs.ModuleNormalizerSpec

class ModuleNormalizerTest extends
	ModuleNormalizerSpec
	with ModuleNormalizerImpl
	with ParboiledParser 
    with Syntax
    with Errors
    with Configs
    with ModuleResolverImpl
	with SignatureJsonSerializer
{
	
	def testedImplementationName = "Substitution module normalizer"

}
