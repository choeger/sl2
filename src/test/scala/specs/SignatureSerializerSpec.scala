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
			
	implicit class SignatureString(str: String) {
		def parsed = {
			parseAst(str).right.get
		}
		
		def parsedAndSerialized() = {
			serialize(str.parsed)
		}
	}

	def equalParsed = IndirectSignatureMatcher
	def equal(expected : AST) = SignatureMatcher(expected)

	case class IndirectSignatureMatcher() extends Matcher[ParseSerializeDeserializeResult] {
		def apply(delivered : ParseSerializeDeserializeResult) = SignatureMatcher(delivered.parsed).apply(delivered.deserialized)
	}
	
	// This signature basically only matches the strings of the signature's identifiers.
	// Therefore, some equal signatures may be recognized as unequal since type variables can
	// be substituted. A more proper implementation may normalizes both signatures before matching.
	case class SignatureMatcher(expected : AST) extends Matcher[AST] {
		def apply(delivered : AST) = {
			var result = true
			
			expected match {
				case Program(parsedSig, _, parsedData, _) => delivered match {
					case Program(deserializedSig, _, deserializedData, _) =>
						result &&= compareSigs(parsedSig, deserializedSig)
						result &&= compareDatas(parsedData, deserializedData)
					}
			}
			
			MatchResult(result, "failure", "neg failure", "ms failure", "neg ms failure")
		}
		
		def compareList[T](lhs : List[T], compare : (T, T) => Boolean, rhs : List[T]) : Boolean = {
			if (lhs.size != rhs.size)
				return false
			if (lhs.size == 0)
				return true;
			
			// compare each pair of lhs and rhs elements
			// return true if all elements were equal
			(lhs, rhs).zipped.map((l, r) => compare(l, r)).reduce((l, r) => l && r)
		}
		
		def compareMap[S, T](lhs : Map[S, T], compare : (T, T) => Boolean, rhs : Map[S, T]) : Boolean = {
			if (lhs.size != rhs.size)
				return false
			
			// compare with each corresponding element
			for ((key, lvalue) <- lhs) {
				rhs.get(key) match {
					case Some(rvalue) => if (!compare(lvalue, rvalue)) return false
					case None         => return false
				}
			}
		
			true
		}
		
		def compareSigs        (lhs : Map [Var, FunctionSig], rhs : Map [Var, FunctionSig]) : Boolean = compareMap (lhs, compareSig        , rhs)
		def compareDatas       (lhs : List[DataDef]         , rhs : List[DataDef]         ) : Boolean = compareList(lhs, compareData       , rhs)
		def compareConstructors(lhs : List[ConstructorDef]  , rhs : List[ConstructorDef]  ) : Boolean = compareList(lhs, compareConstructor, rhs)
		def compareAstTypes    (lhs : List[ASTType]         , rhs : List[ASTType]         ) : Boolean = compareList(lhs, compareAstType    , rhs)
		def compareTypeVars    (lhs : List[TypeVar]         , rhs : List[TypeVar]         ) : Boolean = compareList(lhs, compareTypeVar    , rhs)
		
		// identifiers are matched on string level
		// note that this may be insufficient because of equivalent substittions
		def compareTypeVar(lhs : TypeVar, rhs : TypeVar) : Boolean = lhs.equals(rhs)
		def compareTConVar(lhs : TConVar, rhs : TConVar) : Boolean = lhs.equals(rhs)
		def compareConVar (lhs : TConVar, rhs : TConVar) : Boolean = lhs.equals(rhs)

		def compareSig(lhs : FunctionSig, rhs : FunctionSig) : Boolean = compareAstType(lhs.typ, rhs.typ)
		
		def compareAstType(lhs : ASTType, rhs : ASTType) : Boolean = {
			(lhs, rhs) match {
				case (lhs : TyVar , rhs : TyVar ) => compareTypeVar(lhs.ide, rhs.ide)
				case (lhs : FunTy , rhs : FunTy ) => compareAstTypes(lhs.types, rhs.types)
				case (lhs : TyExpr, rhs : TyExpr) => 
					compareTConVar(lhs.conType, rhs.conType) &&
						compareAstTypes(lhs.typeParams, rhs.typeParams)
				case _ => false
			}
		}
		
		def compareData(lhs : DataDef, rhs : DataDef) : Boolean =
			compareTConVar(lhs.ide, rhs.ide) &&
				compareTypeVars(lhs.tvars, rhs.tvars) &&
				compareConstructors(lhs.constructors, rhs.constructors)
		
		def compareConstructor(lhs : ConstructorDef, rhs : ConstructorDef) : Boolean = 
			compareConVar(lhs.constructor, rhs.constructor) &&
				compareAstTypes(lhs.types, rhs.types)

	}
	
	describe(testedImplementationName() + " signature matcher test (yes we are testing the testers!)") {
		it("Should fail on unequal function identifiers") {
			"FUN f : (X a) -> y".parsed should not equal("FUN g : (X a) -> y".parsed)
		}
		it("Should fail on unequal type lists") {
			"FUN f : (X a) -> y".parsed should not equal("FUN g : (X a) -> y -> z".parsed)
		}
		it("Should fail on unequal type identifiers") {
			"FUN f : (X a) -> y".parsed should not equal("FUN f : (X b) -> z".parsed)
		}
		it("Should fail on unequal type variables") {
			"FUN f : (X a) -> y".parsed should not equal("FUN f : (X b) -> y".parsed)
		}
		it("Should fail on unequal data identifiers in functions") {
			"FUN f : (X a) -> y".parsed should not equal("FUN f : (Y b) -> y".parsed)
		}
		it("Should fail on unequal data identifiers in DATA definitions") {
			"DATA Type a b c = Cons1 a b | Cons2 c".parsed should not equal("DATA Epyt a b c = Cons1 a b | Cons2 c".parsed)
		}
		it("Should fail on unequal constructor identifiers") {
			"DATA Type a b c = Cons1 a b | Cons2 c".parsed should not equal("DATA Type a b c = Cons1 a b | Cons3 c".parsed)
		}
		it("Should fail on unequal constructor lists") {
			"DATA Type a b c = Cons1 a b | Cons2 c".parsed should not equal("DATA Type a b c = Cons1 a b | Cons2 c | Cons 3".parsed)
		}
	}

	describe(testedImplementationName() + " serialize and deserialize") {
		it("Should serialize and deserialize one function signature") {
			"FUN f : (X a) -> y".parsedSerializedAndDeserialized should equalParsed()
		}
		
		it("Should serialize and deserialize one data definition") {
			"DATA Type a b c = Cons1 a b | Cons2 c".parsedSerializedAndDeserialized should equalParsed()
		}
	}
	
}
