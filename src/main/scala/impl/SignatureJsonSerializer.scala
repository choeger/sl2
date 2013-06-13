
package de.tuberlin.uebb.sl2.impl

import scala.util.parsing.json._
import de.tuberlin.uebb.sl2.modules.{ SignatureSerializer, Syntax, Errors }

trait SignatureJsonSerializer extends SignatureSerializer with Syntax with ParboiledParser with Errors {

	def serialize(ast : AST) : String = ast2Json(ast).toString
	
	// TODO throw appropriate exception for None case
	def deserialize(jsonString : String) : AST = JSON.parseFull(jsonString) match {
		case Some(result) =>
			val jsonAst = result.asInstanceOf[JsonImportAst]
			
			json2Ast(jsonAst)
		case None => null
	}
	
	// syntax' json export types
	type JsonExportAst                = JSONObject
	type JsonExportAstType            = Any
	type JsonExportAstTypeList        = JSONArray
	type JsonExportTyVar              = JsonExportTypeVar
	type JsonExportFunTy              = JsonExportAstTypeList
	type JsonExportTyExpr             = JSONObject
	type JsonExportFunctionSig        = JsonExportAstType
	type JsonExportFunctionSigMap     = JSONObject
	type JsonExportDataDef            = JSONObject
	type JsonExportDataDefList        = JSONArray
	type JsonExportConstructorDef     = JSONObject
	type JsonExportConstructorDefList = JSONArray
	
	// syntax' json import types
	type JsonImportAst                = Map[String, Any]
	type JsonImportAstType            = Any
	type JsonImportAstTypeList        = List[JsonImportAstType]
	type JsonImportTyVar              = JsonImportTypeVar
	type JsonImportFunTy              = JsonImportAstTypeList
	type JsonImportTyExpr             = Map[String, Any]
	type JsonImportFunctionSig        = JsonImportAstType
	type JsonImportFunctionSigMap     = Map[String, JsonImportFunctionSig]
	type JsonImportDataDef            = Map[String, Any]
	type JsonImportDataDefList        = List[JsonImportDataDef]
	type JsonImportConstructorDef     = Map[String, Any]
	type JsonImportConstructorDefList = List[JsonImportConstructorDef]
	
	// identifier's json export types
	type JsonExportVar     = String
	type JsonExportTypeVar = String
	type JsonExportTypeVarList = JSONArray
	type JsonExportConVar  = String
	type JsonExportTConVar = String
	
	// identifier's json import types
	type JsonImportVar     = String
	type JsonImportTypeVar = String
	type JsonImportTypeVarList = List[JsonImportTypeVar]
	type JsonImportConVar  = String
	type JsonImportTConVar = String
	
	// identifier conversion to json
	private def     var2Json(ide : Var    ) : JsonExportVar     = ide
	private def typeVar2Json(ide : TypeVar) : JsonExportTypeVar = ide
	private def  conVar2Json(ide : ConVar ) : JsonExportConVar  = ide
	private def tConVar2Json(ide : TConVar) : JsonExportTConVar = ide
	
	// identifier conversion from json	
	private def json2Var    (jsonIde : JsonImportVar    ) : Var     = jsonIde
	private def json2TypeVar(jsonIde : JsonImportTypeVar) : TypeVar = jsonIde
	private def json2ConVar (jsonIde : JsonImportConVar ) : ConVar  = jsonIde
	private def json2TConVar(jsonIde : JsonImportTConVar) : TConVar = jsonIde

	// some map macros
	
	private def typeVars2Json(ides : List[TypeVar]) : JsonExportTypeVarList = JSONArray(ides.map(typeVar2Json))
	
	private def json2TypeVars(jsonIdes : JsonImportTypeVarList) : List[TypeVar] = jsonIdes.map(json2TypeVar)
	
	private def  sigs2Json(sigs  : Map[Var, FunctionSig]) : JsonExportFunctionSigMap     = JSONObject(sigs.map(sig2Json))
	private def types2Json(types : List[ASTType]        ) : JsonExportAstTypeList        = JSONArray(types.map(type2Json))
	private def datas2Json(datas : List[DataDef]        ) : JsonExportDataDefList        = JSONArray(datas.map(data2Json))
	private def ctors2Json(ctors : List[ConstructorDef] ) : JsonExportConstructorDefList = JSONArray(ctors.map(ctor2Json))

	private def json2Sigs (jsonSigs  : JsonImportFunctionSigMap)     : Map[Var, FunctionSig] = jsonSigs .map(json2Sig)
	private def json2Types(jsonTypes : JsonImportAstTypeList)        : List[ASTType]         = jsonTypes.map(json2Type)
	private def json2Datas(jsonDatas : JsonImportDataDefList)        : List[DataDef]         = jsonDatas.map(json2Data)
	private def json2Ctors(jsonCtors : JsonImportConstructorDefList) : List[ConstructorDef]  = jsonCtors.map(json2Ctor)

	// actual (de)serialization implementation starts here
	
	private def ast2Json(ast : AST) : JsonExportAst = ast match { case Program(sigs, _, datas, _) =>
		var root : Map[String, Any] = Map()
		root += ("signatures" -> sigs2Json(sigs))
		root += ("dataDefs"   -> datas2Json(datas))
		
		JSONObject(root)
	}
		
	private def json2Ast(jsonAst : JsonImportAst) : AST = {
		val jsonSigs  = jsonAst.get("signatures").get.asInstanceOf[JsonImportFunctionSigMap]
		val jsonDatas = jsonAst.get("dataDefs"  ).get.asInstanceOf[JsonImportDataDefList]
		
		Program(json2Sigs(jsonSigs), Map(), json2Datas(jsonDatas))
	}

	private def sig2Json(sig : (Var, FunctionSig)) : (JsonExportVar, JsonExportFunctionSig) = {
		val (ide, funSig) = sig
		
		(var2Json(ide), type2Json(funSig.typ))
	}
		
	private def json2Sig(jsonSig : (JsonImportVar, JsonImportFunctionSig)) : (Var, FunctionSig) = {
		var (fun, sig) = jsonSig
		
		(json2Var(fun), FunctionSig(json2Type(sig)))
	}

	private def type2Json(typ : ASTType) : JsonExportAstType = typ match {
		case TyVar(ide, _) => typeVar2Json(ide)
		case TyExpr(con, params, _) =>
			var map : Map[String, Any] = Map()
			map += ("type"   -> tConVar2Json(con))
			map += ("params" -> types2Json(params))
			
			JSONObject(map)
		case FunTy(types, _) => types2Json(types)
	}
		
	private def json2Type(jsonType : JsonImportAstType) : ASTType = jsonType match {
		case jsonTyVar  : JsonImportTyVar  => TyVar(json2TypeVar(jsonTyVar))
		case jsonTyExpr : JsonImportTyExpr =>
			val con    = jsonTyExpr.get("type"  ).get.asInstanceOf[JsonImportTConVar]
			val params = jsonTyExpr.get("params").get.asInstanceOf[JsonImportAstTypeList]
			
			TyExpr(json2TConVar(con), json2Types(params))
		case jsonFunTy : JsonImportFunTy => FunTy(json2Types(jsonFunTy))
	}

	private def data2Json(data : DataDef) : JsonExportDataDef = {
		var map : Map[String, Any] = Map()
		map += ("ide"          -> tConVar2Json (data.ide         ))
		map += ("tvars"        -> typeVars2Json(data.tvars       ))
		map += ("constructors" -> ctors2Json   (data.constructors))
		
		JSONObject(map)
	}
		
	private def json2Data(jsonData : JsonImportDataDef) : DataDef = {
		val jsonIde   = jsonData.get("ide"         ).get.asInstanceOf[JsonImportTConVar]
		val jsonTvars = jsonData.get("tvars"       ).get.asInstanceOf[JsonImportTypeVarList]
		val jsonCtors = jsonData.get("constructors").get.asInstanceOf[JsonImportConstructorDefList]
		
		DataDef(json2TConVar(jsonIde), json2TypeVars(jsonTvars), json2Ctors(jsonCtors))
	}

	private def ctor2Json(ctor : ConstructorDef) : JsonExportConstructorDef = {
		var map : Map[String, Any] = Map()
		map += ("constructor" -> conVar2Json(ctor.constructor))
		map += ("types"       -> types2Json (ctor.types      ))
		
		JSONObject(map)
	}
	
	private def json2Ctor(jsonCtor : JsonImportConstructorDef) : ConstructorDef = {
		val jsonIde   = jsonCtor.get("constructor").get.asInstanceOf[JsonImportConVar]
		val jsonTypes = jsonCtor.get("types"      ).get.asInstanceOf[JsonImportAstTypeList]
		
		ConstructorDef(json2ConVar(jsonIde), json2Types(jsonTypes))
	}

}
