/*
 * Copyright (c) 2012, TU Berlin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of the TU Berlin nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL TU Berlin BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package de.tuberlin.uebb.sl2.modules

import de.tuberlin.uebb.sl2.impl.CombinatorParser
import de.tuberlin.uebb.sl2.modules.Syntax.{VarFirstClass}
import scala.collection.mutable.ListBuffer
import java.io.File
import scala.io.Source
import scala.language.implicitConversions

trait CodeGenerator {
  self: Syntax with JsSyntax with Errors with PreProcessing with NameSupply with Graph[VarFirstClass] =>

  //TODO: the original compiler created tmp variables named _0, _1 ... At the moment we create $0, $1 ...

  def astToJs(ast: AST): JsStmt = ast match { //TODO
    case Program(imports, signatures, functionDefs, functionDefsExtern, dataDefs, attribute) =>
      dataDefsToJs(dataDefs) &
      functionDefsExternToJs(functionDefsExtern) &
      functionDefsToJs(functionDefs) &
      functionSigsToJs(signatures)
  }

  def dataDefsToJs(ds: List[DataDef]): JsStmt = (ds map dataDefToJs).foldLeft(Noop:JsStmt)(_ & _)

  def dataDefToJs(d : DataDef) : JsStmt = {
    if (d.ide == "Bool") {
      /*
       * This is kind of a hack to allow reusage of js-booleans
       */
      val privateDef = 
        JsDef($("True"), JsBool(true)) &
        JsDef($("False"), JsBool(false))
      val publicDef = 
        JsAssignment(JsQualifiedName(JsName("exports"), JsName($("True"))), JsBool(true)) &
        JsAssignment(JsQualifiedName(JsName("exports"), JsName($("False"))), JsBool(false))
      
      if (d.modifier == PublicModifier)
        privateDef & publicDef
      else 
        privateDef
      
    } else {
      val stmts = for ((c,idx) <- d.constructors.zipWithIndex) yield {
        if (c.types.isEmpty) {
          val privateDef = JsDef($(c.constructor), JsNum(idx))
          val publicDef = JsAssignment(JsQualifiedName(JsName("exports"), JsName($(c.constructor))), JsName($(c.constructor)))

          if (d.modifier == PublicModifier) 
            privateDef & publicDef
          else
            privateDef

        } else {
          val k = c.types.size - 1
          //properties
          val props:List[(JsName, JsExpr)] = (for (i <- 0 to k) yield 
            (JsName("_var" + i), JsName("_arg" + i))).toList
        
          val obj = JsObject((JsName("_cid"), JsNum(idx))::props)

          //create anonymous function for partial application
          val args = (for (i <- k to 1 by -1) yield JsName("_arg" + i)).toList
          val anchor:JsStmt = new JsReturn(obj)
          val body = (anchor /: args)((body, arg) => JsFunction("f", arg::Nil, body)) & JsReturn(Some("f"))
          
          val privateDef = JsDef("_" + c.constructor, JsNum(idx)) & JsFunction($(c.constructor), List("_arg0"), body)
          val publicDef = JsAssignment(JsQualifiedName(JsName("exports"), JsName("_" + c.constructor)), JsName("_" + c.constructor)) &
            JsAssignment(JsQualifiedName(JsName("exports"), JsName($(c.constructor))), JsName($(c.constructor)))

          if (d.modifier == PublicModifier)
            privateDef & publicDef
          else
            privateDef
        }
      }
      stmts.foldLeft(Noop:JsStmt)(_ & _)
    }
  }
  
  def functionDefsExternToJs(exDefs: Map[VarName, FunctionDefExtern]) = {
    val defStmts = exDefs.toList.map{case (name, exDef) => JsDef($(name), JsRaw(exDef.externName))}
    JsStmtConcat(defStmts)
  }

  def dep(x : Var, defs : Set[Var], rhs : List[FunctionDef]) : List[(Var, Var)] = {
    for(d <- rhs ; v <- fv(d); if (defs.contains(v)) ) yield (x -> v)
  }

  def functionDefsToJs(functionDefs: Map[VarName, List[FunctionDef]]): JsStmt = {
    val defs = functionDefs.keys.map(Syntax.Var(_)).toSet
    val dependencies = (for(v <- defs; deps <- dep(v, defs, functionDefs(v.ide))) yield deps).toList
    val depGraph = directedGraph(functionDefs.keys.map(Syntax.Var(_)).toSet, dependencies)
    val sorted = topologicalSort(stronglyConnectedComponents(depGraph), depGraph)

    val l = sorted.map {vs =>
      JsStmtConcat(vs.toList.map { v => 
        val funs = functionDefs(v.ide)
        val args = (for (i <- 0 to (funs(0).patterns.length - 1)) yield (JsName("_arg" + i))).toList
        if (args.length != 0) {
          JsFunction($(v.ide), args(0)::Nil, functionBodyToJs(args.tail, args, v.asInstanceOf[Var], funs))
        } else {
          JsDef($(v.ide), JsFunctionCall(JsAnonymousFunction(Nil, expToJs(funs(0).expr, $(v.ide)) & JsReturn(Some($(v.ide))))))
        }
      })
    }

    JsStmtConcat(l.toList)
  }

  def functionSigsToJs(functionSigs: Map[VarName, FunctionSig]): JsStmt = {
    (functionSigs map functionSigToJs).foldLeft(Noop:JsStmt)(_ & _)
  }

  def functionSigToJs(arg: (VarName, FunctionSig)): JsStmt = arg match { 
    case (name: VarName, sig: FunctionSig) =>
      if (sig.modifier == PublicModifier) {
        JsAssignment(JsQualifiedName(JsName("exports"), JsName($(name))), JsName($(name)))
      } else
        Noop:JsStmt
  }
  
  def functionBodyToJs(args : List[JsName], allArgs : List[JsName], v : Var, funs : List[FunctionDef]) : JsStmt = args match {
    case Nil => {
      val l: List[(JsExpr, JsStmt)] = funs.map(f => {
        var body = expToJs(f.expr, "_return") & new JsReturn(JsName("_return"))
        val jsp = (f.patterns.zip(allArgs)).map(x => patternToJs(x._1, x._2))
        val p = jsp.reduce((x, y) => x & y)
        val defs = JsStmtConcat(p.variables)
        (p.condition, defs & body)
      })
      mergeFunctionPatterns(l.reverse)
    }
    case a::rest => {
      new JsReturn(JsAnonymousFunction(a::Nil, functionBodyToJs(rest, allArgs, v, funs)))
    }
  }

  def mergeFunctionPatterns(l: List[(JsExpr, JsStmt)]): JsStmt = l match {
    case Nil => JsThrow("Pattern not exhaustive!")
    case x :: xs => JsIfElse(x._1, x._2, mergeFunctionPatterns(xs))
  }

  def expToJs(expr: Expr, v: String): JsStmt = expr match {
    case Conditional(condition, thenE, elseE, _) =>
      val tmp = freshName()
      val cond = expToJs(condition, tmp)
      cond &
        JsIfElse(JsName(tmp), expToJs(thenE, v), expToJs(elseE, v))

    case Lambda(patterns, expr, _) => {
      /* build a closure */
      val closed = for (x <- fv(expr)) yield JsDef($(x.ide), $(x.ide))

      val args = for (i <- 0 to (patterns.length - 1)) yield (JsName("_arg" + i))
      val jsp = (patterns.zip(args)).map(x => patternToJs(x._1, x._2))
      val p = jsp.reduce((x, y) => x & y)
      val defs = JsStmtConcat(p.variables)
      val tmp = freshName()
      val body = JsIfElse(p.condition, defs & expToJs(expr, tmp), JsThrow("Pattern for lambda expression did not match arguments")) & JsReturn(Some(tmp))
      
      def mergeCurry(args : List[JsName]) : JsExpr = args match {
        case last::Nil => JsAnonymousFunction(last::Nil, body)
        case x::rest => JsAnonymousFunction(x::Nil, JsReturn(Some(mergeCurry(rest))))
      }
             
      JsDef(v, mergeCurry(args.toList))
    }

    case Case(expr, alternatives, _) => {
      val tmp = freshName()
      val lhs = expToJs(expr, tmp)
      lhs & alternativesToJs(v, tmp, alternatives)
    }

    case Let(definitions, body, _) => {
      //TODO: dependency analysis
      val letdefs = definitions.map(x => {
        expToJs(x.rhs, $(x.lhs))
      })
      val b = JsAnonymousFunction(Nil, letdefs & expToJs(body, v) & JsReturn(Some(v)))
      JsDef(v, JsFunctionCall(b))
    }
      
    case App(function, expr, _) => {
      val tmpArg = freshName()
      val arg = expToJs(expr, tmpArg)
      val tmpF = freshName()
      val f = expToJs(function, tmpF)
      f & arg & JsDef(v, JsFunctionCall(tmpF, JsName(tmpArg)))
    }

    case ExVar(Syntax.Var(ide, module), _) =>
	    if(module == LocalMod) {
	      JsDef(v, JsName(escapeJsIde(ide)))
	    } else {
	      JsDef(v, JsQualifiedName(JsName(module), JsName(escapeJsIde(ide))))
	    }
    case ExCon(Syntax.ConVar(con, module) , _) =>
      	if(module == LocalMod) {
	      JsDef(v, JsName(escapeJsIde(con)))
	    } else {
	      JsDef(v, JsQualifiedName(JsName(module), JsName(escapeJsIde(con))))
	    }
    case ConstInt(value, _) => JsDef(v, JsNum(value))
    case ConstReal(value, _) => JsDef(v, JsNum(value))
    case ConstChar(value, _) => JsDef(v, JsStr(value.toString))
    case ConstString(value, _) => JsDef(v, JsStr(value))
    case JavaScript(jsCode, _, _) => JsDef(v, JsAnonymousFunction(Nil, JsReturn(Some(JsRaw(jsCode)))))
  }

  def alternativesToJs(targetVar: String, matchee: String, alternatives: List[Alternative]): JsStmt = alternatives match {
    case alternative :: rest => {
      val pattern = patternToJs(alternative.pattern, matchee)
      val rhs = expToJs(alternative.expr, targetVar)
      val body = JsStmtConcat(pattern.variables ++ (rhs :: Nil))
      JsIfElse(pattern.condition, body, alternativesToJs(targetVar, matchee, rest))
    }
    case Nil => {
      JsThrow("Pattern not exhaustive!")
    }
  }

  def patternToJs(p: Pattern, matchee: JsExpr): JsPattern = p match {
    case PatternVar(x, _) => JsPattern(JsBool(true), JsDef(JsName(escapeJsIde(x)), matchee) :: Nil)
    case PatternExpr(cons, patterns, _) => {
      val subPatterns = for (
        (pattern, idx) <- patterns.zipWithIndex;
        access = JsMemberAccess(matchee, JsStr("_var" + idx))
      ) yield patternToJs(pattern, access)
      val test = 
        if (subPatterns.size > 0) {
          JsBinOp(JsMemberAccess(matchee, JsStr("_cid")), "===", escapeCons(cons,"_"+_))
        } else
          JsBinOp(matchee, "===", escapeCons(cons,$))

      (JsPattern(test, Nil) /: subPatterns)(_ & _)
    }
  }
  
  def escapeCons(cons: ConVar, escFun: (String=>String)) = cons match {
    case Syntax.ConVar(ide, Syntax.LocalMod) => JsName(escFun(ide))
    case Syntax.ConVar(ide, module) => JsQualifiedName(JsName(module), JsName(escFun(ide)))
  }
  
}

