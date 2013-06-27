package de.tuberlin.uebb.sl2.tests.specs

import org.scalatest.matchers._
import org.scalatest.FunSpec
import de.tuberlin.uebb.sl2.modules._

trait ModuleImporterSpec extends FunSpec with ShouldMatchers {
	// TODO: remove SignatureSerializer
	this : ModuleImporter with Syntax with Parser with Errors with SignatureSerializer =>
	
	def testedImplementationName(): String
			
	implicit class SignatureString(str: String) {
		def parsed = {
			parseAst(str).right.get
		}
		
		def parsedAndSerialized() = {
			serialize(str.parsed)
		}
	}

	describe(testedImplementationName() + "") {
		it("Should do sth") {
			val moduleA = """
				DATA DataA = ConsA
				
				FUN funA : DataA
			""".parsed
			
			val moduleB1 = """
				IMPORT "A" AS A1
				DATA DataB1 = ConsB1a A1.DataA | ConsB1b
				FUN funB1a : A1.DataA
				FUN funB1b : DataB1 -> A1.DataA
			""".parsed
			
			val moduleB2 = """
				IMPORT "A" AS A2
				DATA DataB2 = ConsB2a A2.DataA | ConsB2b
				FUN funB2a : A2.DataA -> DataB2
				FUN funB2b : DataB2
			""".parsed
			
			val moduleC = """
				IMPORT "B1" AS B1
				IMPORT "B2" AS B2
				
				FUN funC : B1.DataB1 -> B2.DataB2
				DEF funC x = b2funB2a(B1.funB1b(x))
			""".parsed
			
			val moduleX = """
				IMPORT "A" AS A
				IMPORT "B1" AS B1
				IMPORT "B2" AS B2
				
				FUN funX : A.DataA -> B2.DataB2
				DEF funX = B2.funB2a
			""".parsed
			
			println(serialize(moduleA));
			println(serialize(moduleB1));
			println(serialize(moduleB2));
			println(serialize(moduleC));
			println(serialize(moduleX));
		}
	}
	
}