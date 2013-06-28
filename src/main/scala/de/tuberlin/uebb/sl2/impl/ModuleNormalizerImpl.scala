package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._

trait ModuleNormalizerImpl extends ModuleNormalizer {
	this: Syntax with ModuleResolverImpl =>

	// TODO implement
	def normalizeModules(imports : List[ResolvedImport]) : List[ResolvedImport] = {
		// initial path module map with known modules
		// this will be extended with unknown modules
		var pathMod = buildPathModuleMap(imports)
		// current number of unknown modules
		// this value will be used for generating module names
		var unknownId = 0
		
		// collect unknown modules
		// normalize modules
		imports.map(imp => {
			val modPath = buildModulePathMap(imp)
			// get the set of unknown modules identified by their paths
			val paths = modPath.toSet.map((kv : (ModuleVar, String)) => kv._2)
			val unknowns = paths &~ pathMod.keySet
			
			// give the unknowns a common name
			// add them to the path module map
			for (u <- unknowns) {
				unknownId += 1
				pathMod += ("#" + unknownId -> modPath.get(u).get)
			}
			
			// generate module substitution function
			val sub = normalizeModuleVar(pathMod, modPath)
			// normalize the current module
			normalizeModule(imp, sub)
		})
	}
	
	// TODO implement
	private def normalizeModule(imp : ResolvedImport, sub : ModuleVar => ModuleVar) : ResolvedImport = imp match {
		case ResolvedQualifiedImport(name, path, file, Program(imports, sigs, defs, exts, datas, attrs), ast) =>
			val normImports = normalizeImports(imports, sub)
			val normSigs    = normalizeSigs   (sigs   , sub)
			val normDatas   = normalizeDatas  (datas  , sub)
			val normProg    = Program(normImports, normSigs, defs, exts, normDatas, attrs)
			
			ResolvedQualifiedImport(name, path, file, normProg, ast)
		case ei : ResolvedExternImport => ei
	}
	
	// TODO implement
	private def normalizeImports(imports : List[Import], sub : ModuleVar => ModuleVar) : List[Import] = imports
	
	// TODO implement
	private def normalizeSigs(sigs : Map[VarName, FunctionSig], sub : ModuleVar => ModuleVar) : Map[VarName, FunctionSig] = sigs
	
	// TODO implement
	private def normalizeDatas(datas : List[DataDef], sub : ModuleVar => ModuleVar) : List[DataDef] = datas
	
	// TODO implement
	private def normalizeModuleVar(pathMod : Map[String, ModuleVar], modPath : Map[ModuleVar, String]) : ModuleVar => ModuleVar = (ide => ide)
	
	// TODO implement
	private def buildPathModuleMap(imports : List[ResolvedImport]) : Map[String, ModuleVar] = Map()
	
	// TODO implement
	private def buildModulePathMap(imports : ResolvedImport) : Map[ModuleVar, String] = Map()

}
