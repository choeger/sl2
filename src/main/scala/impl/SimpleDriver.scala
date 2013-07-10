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
  * Simple compiler driver.
  */
trait SimpleDriver extends Driver {

  self: Parser with CodeGenerator with Syntax with ProgramChecker with JsSyntax with Errors =>

  override def run(input: List[String]): Either[Error, String] = {
    
    def parseProgram(in: String): Either[Error, Program] = for ( ast <- parseAst(in).right ) yield ast.asInstanceOf[Program]

    def mergePrograms(progs: List[Program]): Either[Error, Program] = {
      progs.foldLeft[Either[Error, Program]](Right(emptyProgram))((z, x) => z.right.flatMap(y => mergeAst(y, x)))
    }

    for ( programs <- errorMap(input, (in: String) => parseProgram(in)).right ;
	  ast <- mergePrograms(programs).right ;
	  _ <- checkProgram(ast).right )
    yield JsPrettyPrinter.pretty(astToJs(ast) & JsFunctionCall("$main"))
  }

  /**
    * Merge the signatures and top-level definitions of two programs.
    *
    * @return The merged programs, or a DuplicateError if the two programs have name
    *         clashes in their top-level function and data type definitions.
    */
  def mergeAst(a: Program, b: Program): Either[Error, Program] = {
    for ( sigs <- mergeMap(a.signatures, b.signatures).right;
	  funs <- mergeMap(a.functionDefs, b.functionDefs).right )
    yield {
      val defs = a.dataDefs ++ b.dataDefs
      Program(sigs, funs, defs)
    }
  }

  def mergeMap[A, B](a: Map[A, B], b: Map[A, B]): Either[Error, Map[A, B]] = {
    val intersect = a.keySet & b.keySet
    if (intersect.isEmpty)
      Right(a ++ b)
    else
      Left(DuplicateError("Duplicated definition: " + intersect.mkString(", "), "", Nil))
  }
}

