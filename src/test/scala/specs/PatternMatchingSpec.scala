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

package de.tuberlin.uebb.sl2.tests.specs

import org.scalatest.matchers._
import org.scalatest.{ FunSpec, Inside }

import de.tuberlin.uebb.sl2.modules._

trait PatternMatchingSpec extends FunSpec with Inside with ShouldMatchers {

  this : Syntax with PatternMatching => 

  private def cons(hd : String, tl : String) = { PatternExpr("Cons", PatternVar(hd)::PatternVar(tl)::Nil) }
  
  private def nil = PatternExpr("Nil", Nil)

  def arity(c : ConVar) = c match {
    case "Cons" => 2
    case _ => 0
  }

  def constructors(c : ConVar) = c match {
    case "Cons" | "Nil" => Set("Cons", "Nil")
    case _ => Set[ConVar]()
  }

  val ctxt = PatternMatchingCtxt(arity, constructors, 1)

  val demoeq = List(Equation(PatternVar("f")::nil::PatternVar("ys")::Nil, ExVar("a")),
                    Equation(PatternVar("f")::PatternVar("xs")::nil::Nil, ExVar("b")),
                    Equation(PatternVar("f")::cons("x", "xs")::cons("y","ys")::Nil, ExVar("c")))

  describe("The pattern matching optimizer") {
    it("Should create a simple constant match expression") {
      val testeq = Equation(PatternExpr("Nil", Nil)::Nil, ExVar("y"))::Nil
      inside(createSimpleCaseMatch(ctxt, testeq, List("x"))) { 
        case Case(_, alt1::alt2::Nil, _) => {
          alt1 should be(Alternative(cons("u1", "u2"), ExVar("MATCHFAIL")))          
          alt2 should be(Alternative(nil, ExVar("y")))
        }
     }
    }

    it("should forward nested patterns") {
      val testeq = Equation(PatternExpr("Cons", PatternVar("hd")::PatternExpr("Nil", Nil)::Nil)::Nil, ExVar("y"))::Nil
      inside(createSimpleCaseMatch(ctxt.copy(k=2), testeq, List("u1"))) {
        case Case(_, alt1::alt2::Nil, _) => {
          alt2.pattern should be (nil)
          alt2.expr should be (ExVar("MATCHFAIL"))

          alt1.pattern should be (cons("u2", "u3"))
          inside(alt1.expr) {
            case Case (_, alt1::alt2::Nil, _) => {
              alt1.pattern should be (cons("u4", "u5"))
              alt1.expr should be (ExVar("MATCHFAIL"))

              alt2.pattern should be (nil)
              alt2.expr should be(ExVar("y"))
            }
          }
        }
      }     
    }

    it("Should simply rename variables") {
      val testeq = Equation(PatternVar("x")::Nil, ExVar("x"))::Nil
      createSimpleCaseMatch(ctxt, testeq, List("y")) should be(ExVar("y"))
    }

    it("Should work correctly on Wadler's demo") {

      inside(createSimpleCaseMatch(ctxt.copy(k=4), demoeq, List("u1", "u2", "u3"))) {
        case Case(v, alt1::alt2::Nil, _) => {
          v should be (ExVar("u2"))
          alt2.pattern should be (nil)
          alt1.pattern should be (cons("u4", "u5"))
          
          alt2.expr should be(ExVar("a"))
          inside (alt1.expr) {
            case Case(v, alt1::alt2::Nil, _) => {
              v should be(ExVar("u3"))
              alt2.pattern should be (nil)
              alt2.expr should be(ExVar("b"))
              
              alt1.pattern should be (cons("u4", "u5"))
              
              inside (alt1.expr) {
                case Case(v, alt1::alt2::Nil, _) => {
                  v should be(ExVar("u2"))
                  alt2.pattern should be(nil)
                  alt2.expr should be (ExVar("MATCHFAIL"))

                  alt1.pattern should be(cons("u4","u5"))
                  inside(alt1.expr) {
                    case Case(v, alt1::alt2::Nil,_) => {
                      v should be (ExVar("u3"))
                      alt2.pattern should be (nil)
                      alt2.expr should be(ExVar("MATCHFAIL"))
                      
                      alt1.pattern should be(cons("u6", "u7"))
                      alt1.expr should be (ExVar("c"))
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  

  }
}

