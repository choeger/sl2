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

package de.tuberlin.uebb.sl2.impl

import de.tuberlin.uebb.sl2.modules._

/**
  * Port of P. Wadler, Efficient Compilation of Pattern Matching
  */
trait WadlerPatternMatching extends PatternMatching with Syntax with AlphaConversion {

  def createSimpleCaseMatch(ctxt: PatternMatchingCtxt, equations: List[Equation], variables: List[Var]) = {
    wadlerMatch(ctxt, variables, equations, ExVar(Syntax.Var("MATCHFAIL")))
  }

  def isVar(eq: Equation) = eq.pattern match {
    case PatternVar(_, _) :: _ => true
    case _ => false
  }

  def partition[A](p: A => Boolean, l: List[A]): List[List[A]] = l match {
    case Nil => Nil
    case x :: Nil => List(List(x))
    case x :: y :: xs if (p(x) && p(y)) => tack(x, partition(p, y :: xs))
    case x :: y :: xs => List(x) :: partition(p, y :: xs)
  }

  def tack[A](x: A, xss: List[List[A]]): List[List[A]] = {
    (x :: xss.head) :: xss.tail
  }

  def wadlerMatch(ctxt: PatternMatchingCtxt, variables: List[Var], equations: List[Equation], error: Expr): Expr = variables match {
    case Nil => equations match {
      case eq :: Nil => eq.rhs
      case eq :: rest => eq.rhs //TODO: Warn about unreachable code
      case Nil => error
    }
    case u :: us => partition(isVar, equations).foldRight(error)(matchVarCon(ctxt, variables))
  }

  def matchVarCon(ctxt: PatternMatchingCtxt, variables: List[Var])(equations: List[Equation], error: Expr) = {
    if (isVar(equations.head)) {
      matchVar(ctxt, variables, equations, error)
    } else {
      matchCon(ctxt, variables, equations, error)
    }
  }

  def matchVar(ctxt: PatternMatchingCtxt, variables: List[Var], equations: List[Equation], error: Expr) = variables match {
    case u :: us => {
      object freshNames {
        var count = ctxt.k - 1
        def fresh(u: Unit): String = {
          count = count + 1
          "u" + count
        }
      }
      val newEquations = equations map {
        case Equation(PatternVar(f, _) :: patterns, expr) =>
          Equation(patterns, substitute(freshNames.fresh, Map(f -> u.ide), expr))
        case _ =>
          sys.error("Pattern Matching: Empty pattern list while building matching equation.")
      }
      wadlerMatch(ctxt, us, newEquations, error)
    }

    case Nil => sys.error("Pattern matching: Empty variable list while matching variables.")
  }

  def choose(c: ConVar, equations: List[Equation]): List[Equation] = equations match {
    case Nil => Nil
    case (eq @ Equation(PatternExpr(x, _, _) :: _, _)) :: rest if (x == c) => eq :: choose(c, rest)
    case _ :: rest => choose(c, rest)
  }

  def getCon(eq: Equation) = eq match {
    case Equation(PatternExpr(c, _, _) :: _, _) => c
    case Equation(PatternVar(_, _) :: _, _) => sys.error("Pattern Matching: Cannot select constructor in pattern variable.")
    case Equation(Nil, _) => sys.error("Pattern Matching: Cannot select constructor in empty equation.")
  }

  def matchCon(ctxt: PatternMatchingCtxt, variables: List[Var], equations: List[Equation], error: Expr) = variables match {
    case u :: us => {
      val cs = ctxt.constructors(getCon(equations.head))
      val alternatives = cs map { c => matchClause(c, ctxt, variables, choose(c, equations), error) }

      Case(ExVar(u), alternatives.toList)
    }
    case Nil => sys.error("Pattern matching: Cannot match constructors in empty pattern list.")
  }

  def matchClause(c: ConVar, ctxt: PatternMatchingCtxt, variables: List[Var], equations: List[Equation], error: Expr): Alternative = variables match {
    case u :: us => {
      val kn = ctxt.k + ctxt.arity(c)
      val newVars = (ctxt.k until kn).toList.map("u" + _)

      val newEquations = for (eq <- equations) yield eq match {
        case Equation(PatternExpr(con, exprs, _) :: pattern, rhs) => Equation(exprs ++ pattern, rhs)
        case Equation(PatternVar(_, _) :: _, _) => sys.error("Pattern Matching: Cannot match clauses in pattern variable.")
        case Equation(Nil, _) => sys.error("Pattern matching: Cannot match clauses in empty equation.")
      }

      Alternative(PatternExpr(c, newVars map (PatternVar(_))), wadlerMatch(ctxt.copy(k = kn),
        newVars.map(Syntax.Var(_)) ++ us, newEquations, error))
    }
    case Nil => sys.error("Pattern matching: Cannot match clauses in empty pattern list.")
  }

}
