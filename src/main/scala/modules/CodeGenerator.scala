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
 * */

package de.tuberlin.uebb.sl2.modules

import de.tuberlin.uebb.sl2.impl.CombinatorParser
import scala.language.implicitConversions
import scala.collection.mutable.ListBuffer
import de.tuberlin.uebb.sl2.modules.Syntax.{Var}

trait CodeGenerator {
  self: Syntax with JsSyntax with Errors with PreProcessing with NameSupply with Graph[Var] =>

  //TODO: the original compiler created tmp variables named _0, _1 ... At the moment we create $0, $1 ...

  def astToJs(ast: AST): JsStmt = ast match { //TODO
    case Program(signatures, functionDefs, dataDefs, attribute) => dataDefsToJs(dataDefs) & functionDefsToJs(functionDefs)
  }

  def dataDefsToJs(ds: List[DataDef]): JsStmt = (ds map dataDefToJs).foldLeft(Noop:JsStmt)(_ & _)

  object BooleanDef {
    def unapply(d : DataDef) = {
      d.ide == "Bool" //should suffice, since builtin types cannot be overriden...
    }
  }

  def dataDefToJs(d : DataDef) : JsStmt = d match {
    case BooleanDef() => {
      /*
       * This is kind of a hack to allow reusage of js-booleans
       */
      JsDef($("True"), JsBool(true)) &
      JsDef($("False"), JsBool(false))      
    }
    case _ => {
      val stmts = for ((c,idx) <- d.constructors.zipWithIndex) yield {
        if (c.types.isEmpty) {
          JsDef($(c.constructor), JsNum(idx))
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
          
          JsDef("_" + c.constructor, JsNum(idx)) & JsFunction($(c.constructor), List("_arg0"), body)
        }
      }
      stmts.foldLeft(Noop:JsStmt)(_ & _)
    }
  }

  def dep(x : Var, defs : Set[Var], rhs : List[FunctionDef]) : List[(Var, Var)] = {
    for(d <- rhs ; v <- fv(d); if (defs.contains(v)) ) yield (x -> v)
  }

  def functionDefsToJs(functionDefs: Map[Var, List[FunctionDef]]): JsStmt = {
    val defs = functionDefs.keys.toSet
    val dependencies = (for(v <- defs; deps <- dep(v, defs, functionDefs(v))) yield deps).toList
    val depGraph = directedGraph(functionDefs.keys.toSet, dependencies)
    val sorted = topologicalSort(stronglyConnectedComponents(depGraph), depGraph)

    val l = sorted.map {vs =>
      JsStmtConcat(vs.toList.map { v => 
        val funs = functionDefs(v)
        val args = (for (i <- 0 to (funs(0).patterns.length - 1)) yield (JsName("_arg" + i))).toList
        if (args.length != 0) {
          new JsFunction($(v), args(0)::Nil, functionBodyToJs(args.tail, args, v, funs))
        } else {
          JsDef($(v), JsFunctionCall(JsAnonymousFunction(Nil, expToJs(funs(0).expr, $(v)) & JsReturn(Some($(v))))))
        }
      })
    }

    JsStmtConcat(l.toList)
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
      val closed = for (x <- fv(expr)) yield JsDef($(x), $(x))

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

    case ExVar(ide, _) => JsDef(v, JsName(escapeJsIde(ide)))
    case ExCon(con, _) => JsDef(v, JsName(escapeJsIde(con)))
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
          JsBinOp(JsMemberAccess(matchee, JsStr("_cid")), "===", "_" + cons)
        } else
          JsBinOp(matchee, "===", $(cons))

      (JsPattern(test, Nil) /: subPatterns)(_ & _)
    }
  }

}

