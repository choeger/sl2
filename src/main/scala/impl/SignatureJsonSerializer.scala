
package de.tuberlin.uebb.sl2.impl

import scala.util.parsing.json._
import de.tuberlin.uebb.sl2.modules.{ SignatureSerializer, Syntax, Errors }

trait SignatureJsonSerializer extends SignatureSerializer with Syntax with ParboiledParser with Errors {

	def serialize(ast : AST) : String = ast2Json(ast).toString
	
	def deserialize(jsonString : String) : AST = Program(null, null, null, null)
	
	private def ast2Json(ast : AST) : JSONType = ast match { case Program(sigs, _, datas, _) =>
		var root : Map[String, Any] = Map()
		root += ("signatures" -> sigs2Json(sigs))
		root += ("dataDefs"   -> datas2Json(datas))
		
		new JSONObject(root)
	}
	
	private def sigs2Json(sigs : Map[Var, FunctionSig]) : JSONType = JSONObject(sigs.mapValues(sig2Json))
	
	private def sig2Json(sig : FunctionSig) : JSONType = JSONObject(Map[String, Any]())
	
	private def datas2Json(data : List[DataDef]) : JSONType = JSONArray(List[Any]())
	
	private def json2sigs(json : JSONObject) : Map[Var, FunctionSig] = null
	
	private def json2sig(json : JSONObject) : FunctionSig = null

}
