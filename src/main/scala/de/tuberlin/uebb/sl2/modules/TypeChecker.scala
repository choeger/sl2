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

/**
 * The SL type checker.
 */
trait TypeChecker {

  this: Lexic with Syntax with Context with EnrichedLambdaCalculus with Type with Errors =>

  /**
   * Type inference on the enriched lambda calculus.
   */
  def checkTypes(ctx: Context, e: ELC): Either[Error, Type]

  /**
   * Context containing the types for all predefined SL functions and operators.
   */
  def predefsContext: Context = {
    val int = BaseType(Integer)
    val real = BaseType(Real)
    val str = BaseType(String)
    val char = BaseType(Character)
    val bool = TypeConstructor(Syntax.TConVar("Bool"), Nil)
    val α = TypeVariable("a")
    val β = TypeVariable("b")
    val dom = (t: Type) => TypeConstructor(Syntax.TConVar("DOM"), List(t))

    Map(
      Syntax.Var(addLex) -> (int --> (int --> int)),
      Syntax.Var(subLex) -> (int --> (int --> int)),
      Syntax.Var(mulLex) -> (int --> (int --> int)),
      Syntax.Var(divLex) -> (int --> (int --> int)),

      Syntax.Var(realDiv) -> (real --> (real --> real)),
      Syntax.Var(realSub) -> (real --> (real --> real)),
      Syntax.Var(realMul) -> (real --> (real --> real)),
      Syntax.Var(realAdd) -> (real --> (real --> real)),

      Syntax.Var(ltLex) -> (int --> (int --> bool)),
      Syntax.Var(leLex) -> (int --> (int --> bool)),
      Syntax.Var(eqLex) -> (int --> (int --> bool)),
      Syntax.Var(neLex) -> (int --> (int --> bool)),
      Syntax.Var(geLex) -> (int --> (int --> bool)),
      Syntax.Var(gtLex) -> (int --> (int --> bool)),
      Syntax.Var(strAdd) -> (str --> (str --> str)),
      Syntax.Var(yieldLex) -> forall(α)(α --> dom(α)),
      Syntax.Var(bindLex) -> forall(α, β)(dom(α) --> ((α --> dom(β)) --> dom(β))),
      Syntax.Var(bindNRLex) -> forall(α, β)(dom(α) --> (dom(β) --> dom(β))),
      Syntax.Var(ordLex) -> (char --> int),
      Syntax.Var(chrLex) -> (int --> char) // TODO: String to char list functions. Prelude? Do we need them here?
      )
  }
}
