package de.tuberlin.uebb.sl2.tests.specs

import org.scalatest.matchers._
import org.scalatest.FunSpec
import scala.language.implicitConversions
import de.tuberlin.uebb.sl2.modules._

trait SignatureSerializerSpec extends FunSpec with ShouldMatchers {
	this : SignatureSerializer with Syntax with Parser with Errors =>

	def testedImplementationName(): String
	
	case class ParseSerializeDeserializeResult(parsed : AST, serialized : String, deserialized : AST)
	
	implicit class ParsingInput(s: String) {
		def parsedSerializedAndDeserialized() = {
			val ast = parseAst(s).right.get
			val serialized = serialize(ast)
			val deserialized = deserialize(serialized)
			
			ParseSerializeDeserializeResult(ast, serialized, deserialized)
		}
	}
	
	case class SignatureMatcher() extends Matcher[ParseSerializeDeserializeResult] {
		def apply(delivered : ParseSerializeDeserializeResult) = {
			println(delivered.serialized)
		
			var result = true
			
			delivered.parsed match {
				case Program(parsedSig, _, parsedData, _) => delivered.deserialized match {
					case Program(deserializedSig, _, deserializedData, _) =>
						result &&= compareSignature(parsedSig, deserializedSig)
						result &&= compareData(parsedData, deserializedData)
					}
			}
			
			MatchResult(result, "failure", "neg failure", "ms failure", "neg ms failure")
		}
		
		def compareSignature(parsedSig : Map[Var, FunctionSig], deserializedSig : Map[Var, FunctionSig]) : Boolean = {
			if (parsedSig == null)
				println("parsedSig == null");
			if (deserializedSig == null)
				println("deserializedSig == null");
			
			if (parsedSig.size != deserializedSig.size)
				return false
			
			for ((key, value) <- parsedSig) {
			}
		
			true
		}
		
		def compareData(parsedData : List[DataDef], deserializedData : List[DataDef]) : Boolean = {
			true
		}
	}
	
	def equalParsed = SignatureMatcher
	
	implicit class SignatureString(str: String) {
		def parsedAndSerialized() = {
			val ast = parseAst(str).right.get
			
			serialize(ast)
		}
	}

	describe(testedImplementationName() + " stub") {
		it("test2") {
			"FUN f : x -> y".parsedSerializedAndDeserialized should equalParsed()
		}
	}
	
}
