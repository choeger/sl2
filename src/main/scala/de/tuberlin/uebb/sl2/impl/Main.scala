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

import scala.collection.mutable.ListBuffer
import de.tuberlin.uebb.sl2.modules._
import de.tuberlin.uebb.sl2.impl._
import de.tuberlin.uebb.sl2.modules.Syntax.{VarFirstClass}
import scala.io.Source

object Main
    extends ParboiledParser 
    with CodeGenerator
    with Syntax
    with SyntaxTraversal
    with Errors
    with JsSyntax
    with PreProcessing
    with Lexic
    with EnrichedLambdaCalculus
    with Type
    with NameSupply
    with Context
    with Substitution
    with Unification
    with GraphImpl[VarFirstClass]
    with LetRecSplitter
    with DTCheckerImpl
    with FDCheckerImpl
    with TypeCheckerImpl
    with ProgramCheckerImpl
    with SimpleDriver
    with SignatureJsonSerializer {

  val usage = """Usage:B <sl> [-d destination directory] source file(s)"""

  def main(args: Array[String]) {
    if (args.isEmpty)
      println(usage)
    else {
      val config = parseArguments(args.toList)
      val input = config("src").asInstanceOf[List[String]]
      //val prelude = Source.fromURL(getClass.getResource("/prelude.sl")).getLines.mkString("\n")
      val preludeJs = Source.fromURL(getClass.getResource("/prelude.js")).getLines.mkString("\n")
      val res = run(input.toList, config)     
      if (res.isLeft)
        res.left.map(x => println("Error: " + x))
      else
        res.right.map(x => println(x))
    }
  }
  
  def parseArguments(args: List[String]): Map[String, Any] = args match {
  	case "-d" :: dir ::  rt => parseArguments(rt) + (("destination", dir))
  	case src ::  rt => {
  		val res = parseArguments(rt)
  		res + (("src", src :: res("src").asInstanceOf[List[String]]))
  	}
    case Nil => defaultConfig
  }
  
  val defaultConfig: Map[String, Any] = Map(
      ("destination", ""),
      ("src", List())) 
}

