package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._

trait ModuleNormalizerImpl extends ModuleNormalizer {
	this: Syntax with ModuleResolverImpl =>

	// TODO implement
	def normalizeModules(imports : List[ResolvedImport]) : List[ResolvedImport] = imports
	
	// TODO implement
	private def normalizeModule(imp : ResolvedImport, pathModuleMap : Map[String, ModuleVar]) : ResolvedImport = imp
	
	// TODO implement
	private def normalizeSigs(sigs : Map[VarName, FunctionSig], sub : ModuleVar => ModuleVar) : Map[VarName, FunctionSig] = sigs
	
	// TODO implement
	private def normalizeDatas(datas : List[DataDef], sub : ModuleVar => ModuleVar) : List[DataDef] = datas
	
	// TODO implement
	private def normalizeModuleVar(pathMod : Map[String, ModuleVar], modPath : Map[ModuleVar, String])(ide : ModuleVar) : ModuleVar = ide
	
	// TODO implement
	private def buildPathModuleMap(imports : List[ResolvedImport]) : Map[String, ModuleVar] = Map()
	
	// TODO implement
	private def buildModulePathMap(imports : List[ResolvedImport]) : Map[ModuleVar, String] = Map()

}
