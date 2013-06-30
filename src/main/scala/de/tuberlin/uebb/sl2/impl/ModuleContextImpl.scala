package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._

trait ModuleContextImpl extends ModuleContext {
  this : ModuleResolver with Syntax with Context with Type =>
  
  def buildModuleContext(imp : List[ResolvedImport]) : (Context, Map[Var, FunctionSig]) = {
    if (imp.isEmpty) return (Map(), Map())
    
    val contexts = imp.map(buildModuleContext)
    val sigs     = imp.map(buildModuleSig)
    
    val context = contexts.reduce(_ <++> _)
    val sig     = sigs    .reduce(_  ++  _)
    
    (context, sig)
  }
  
  def buildModuleContext(imp : ResolvedImport) : Context = imp match {
    case ResolvedQualifiedImport(name, _, _, prog, _) =>
        // this is almost the same implementation as in DTCheckerImpl:dataConTypes
        // note that the difference is in the construction of TConVar and ConVar
        // where the module name is set
        
        var context: Context = Map.empty

        for (
          dataDef <- prog.dataDefs;
          conDef <- dataDef.constructors
        ) {
          val resultType = TypeConstructor(Syntax.TConVar(dataDef.ide, name), dataDef.tvars map TypeVariable)
          val argTypes = conDef.types map astToType
          val conType = argTypes.:+(resultType) reduceRight FunctionType

          // Generalize type over its free variables
          val conTypeScheme = TypeScheme(conType.freeVars, conType)

          context = context + (Syntax.ConVar(conDef.constructor, name) -> conTypeScheme)
        }

        context
    case _ : ResolvedExternImport => Map()
  }
    
  def buildModuleSig(imp : ResolvedImport) : Map[Var, FunctionSig] = imp match {
    case ResolvedQualifiedImport(name, _, _, prog, _) =>
      prog.signatures.map(kv => {
        val (ide, sig) = kv
        
        (Syntax.Var(ide, name) -> sig)
      })
    case _ : ResolvedExternImport => Map()
  }

}
