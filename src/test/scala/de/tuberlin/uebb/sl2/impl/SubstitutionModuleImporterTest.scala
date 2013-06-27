package de.tuberlin.uebb.sl2.tests.impl

import de.tuberlin.uebb.sl2.tests.specs.ModuleImporterSpec
import de.tuberlin.uebb.sl2.impl._

class SubstitutionModuleImporterTest extends ModuleImporterSpec with SubstitutionModuleImporter with ParboiledParser with SignatureJsonSerializer {
	
	def testedImplementationName = "Substitution module importer"

}
