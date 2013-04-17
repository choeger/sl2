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
  * Program checker.
  *
  * Perform context analysis on the program's definitions.
  */
trait ProgramCheckerImpl extends ProgramChecker {

  this: Lexic with Syntax with Context with Type with EnrichedLambdaCalculus with DTChecker with FDChecker with LetRecSplitter with TypeChecker with Errors =>

  /**
    * Context analysis, performing the following checks on a program:
    * $ - Data type checking,
    * $ - Type checking of function definitions,
    * $ - Checking the type of a program's main function.
    */
  def checkProgram(in: AST): Either[Error, Unit] = {
    for ( initialContext <- checkDataTypes(in).right ;
	  (funSigs, funDefs) <- checkFunctions(in).right ;
	  elc <- splitLetRecs(predefinedFuns.toSet, programToELC(funSigs, funDefs)).right ;
	  mainType <- {
	    checkTypes(initialContext <++> predefsContext, elc).right
	  } ;
	  _ <- checkMain(funSigs, mainType).right )
    yield ()
  }

  
  /**
    * Check if the `main' function's type is `DOM Void'.
    */
  def checkMain(signatures: Map[Var, FunctionSig], inferredType: Type): Either[Error, Unit] = {
    
    val checkInferredType = inferredType match {
      case TypeConstructor("DOM", List(BaseType(Void))) => Right()
      case _ => Left(GenericError("Function `main' must be of type `DOM Void', but found: " + quote(inferredType.toString)))
    }

    val checkMainSignature = signatures.get("main") match {
      case None => Right()
      case Some(FunctionSig(signature, attr)) => {
	val mainSignature = astToType(signature)
	if (mainSignature == inferredType) Right()
	else Left(AttributedError("Could not match declared type " + quote(mainSignature.toString) + " against inferred type " + quote(inferredType.toString) + " in `main'", attr))
      }
    }

    for ( _ <- checkInferredType.right ;
	  _ <- checkMainSignature.right )
    yield ()
  }
}

