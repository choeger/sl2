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
    val int  = BaseType(Integer)
    val real = BaseType(Real)
    val str  = BaseType(String)
    val char = BaseType(Character)
    val bool = TypeConstructor("Bool", Nil)
    val α    = TypeVariable("a")
    val β    = TypeVariable("b")
    val dom  = (t: Type) => TypeConstructor("DOM", List(t))

    Map( addLex    -> (int --> (int --> int)),
	 subLex    -> (int --> (int --> int)),
         mulLex    -> (int --> (int --> int)),
         divLex    -> (int --> (int --> int)),
         
         realDiv   -> (real --> (real --> real)),
	 realSub   -> (real --> (real --> real)),
         realMul   -> (real --> (real --> real)),
         realAdd   -> (real --> (real --> real)),

         ltLex     -> (int --> (int --> bool)),
         leLex     -> (int --> (int --> bool)),
	 eqLex     -> (int --> (int --> bool)),
	 neLex     -> (int --> (int --> bool)),
	 geLex     -> (int --> (int --> bool)),
	 gtLex     -> (int --> (int --> bool)),
         strAdd    -> (str --> (str --> str)),
	 yieldLex  -> forall(α)(α --> dom(α)),
	 bindLex   -> forall(α, β)(dom(α) --> ((α --> dom(β)) --> dom(β))),
	 bindNRLex -> forall(α, β)(dom(α) --> (dom(β) --> dom(β))),
	 ordLex    -> (char --> int),
	 chrLex    -> (int --> char)
	 // TODO: String to char list functions. Prelude? Do we need them here?
       )
  }
}
