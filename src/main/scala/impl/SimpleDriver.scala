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

package de.tuberlin.uebb.sl2.impl

import scala.collection.mutable.ListBuffer
import de.tuberlin.uebb.sl2.modules._

trait SimpleDriver extends Driver{
  self: Parser with CodeGenerator with Syntax with ProgramChecker with JsSyntax with Errors =>

  override def run(input: List[String]): Either[Error, String] =
    {
      val ast: ListBuffer[Program] = new ListBuffer
      for (f <- input) {
        parseAst(f) match {
          case Right(a) => ast += a.asInstanceOf[Program]
          case Left(a) => return Left(a)
        }
      }
      val m = ast.foldLeft[Either[Error, Program]](Right(Program(Map(), Map(), Nil)))((z, x) =>
        z.right.flatMap(y => mergeAst(y, x)))
      for (
        mo <- m.right;
        _ <- checkProgram(mo).right
      ) yield JsPrettyPrinter.pretty(astToJs(mo) & JsFunctionCall("$main"))
    }

  def mergeAst(a: Program, b: Program): Either[Error, Program] =
    {
      for (
        sigs <- mergeMap(a.signatures, b.signatures).right;
        funs <- mergeMap(a.functionDefs, b.functionDefs).right
      ) yield {
        val defs = a.dataDefs ++ b.dataDefs
        Program(sigs, funs, defs)
      }

    }

  def mergeMap[A, B](a: Map[A, B], b: Map[A, B]): Either[Error, Map[A, B]] =
    {
      val intersect = a.keySet & a.keySet
      if (intersect.isEmpty)
        Right(a ++ b)
      else
        Left(DuplicateError("Duplicated definition: " + intersect.mkString(", "), "", Nil))
    }
}

