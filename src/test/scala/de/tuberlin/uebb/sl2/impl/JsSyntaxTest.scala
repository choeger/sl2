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

package de.tuberlin.uebb.sl2.tests.impl

import org.scalatest.matchers._
import org.scalatest.{ FunSpec, Inside }
import de.tuberlin.uebb.sl2.modules._
import org.mozilla.javascript._
import scala.language.postfixOps
import java.io.StringReader
import org.mozilla.javascript.CompilerEnvirons
import org.mozilla.javascript.Parser
import org.mozilla.javascript.ast.AstRoot

class JsSyntaxTest extends FunSpec with Inside with ShouldMatchers with JsSyntax {

  implicit class rhinoString(s: String) {
    val compilerEnv = new CompilerEnvirons();
    val errorReporter = compilerEnv.getErrorReporter();
    val parser = new Parser(compilerEnv, errorReporter);
    def parseWithRhinoWrappedInAssignment: String = {
      val wrapped = "var a = " + s
      val result = parser.parse(wrapped, null, 1);
      result.toSource()
    }

    def parseWithRhinoWrappedInFunction: String = {
      val wrapped = "function(){" + s + "}"
      val result = parser.parse(wrapped, null, 1);
      result.toSource()
    }
    def parseWithRhino: String = {
      val result = parser.parse(s, null, 1);
      result.toSource()
    }
  }

  implicit class JsInput(s: JsStmt) {
    def asString = JsPrettyPrinter.pretty(s)
  }

  def testedImplementationName(): String = "JsSytanxTest"

  describe(testedImplementationName() + " Test case 1: Basic tests") {
    it("Should print integer literals") {
      JsNum(23).asString.parseWithRhino should equal("23" parseWithRhino)
    }

    it("Should print string literals") {
      JsStr("foobar").asString.parseWithRhino should equal(""""foobar"""" parseWithRhino)
    }

    it("Should print bool literals") {
      JsBool(true).asString.parseWithRhino should equal("true" parseWithRhino)
    }

    it("Should print null") {
      JsNull.asString.parseWithRhino should equal("null" parseWithRhino)
    }

    it("Should print prefix operator") {
      JsUnOp(JsName("-"), JsNum(42)).asString.parseWithRhino should equal("(-42)" parseWithRhino)
    }

    it("Should print binary operator") {
      JsBinOp(JsNum(23), JsName("+"), JsNum(42)).asString.parseWithRhino should equal("(23+42)" parseWithRhino)
    }

    it("Should print anonymous function") {
      JsAnonymousFunction(List(JsName("a"), JsName("b")), new JsReturn(JsName("a"))).asString.parseWithRhino should equal("function(a,b){return a}" parseWithRhino)
    }

    it("Should print a simple object") {
      JsObject(List(("first", JsNum(1)), ("second", JsNum(2)))).asString.parseWithRhinoWrappedInAssignment should equal("{first: 1, second: 2}".parseWithRhinoWrappedInAssignment)
    }

    it("Should print a simple member access") {
      JsMemberAccess("array", JsNum(1)).asString.parseWithRhino should equal("array[1]".parseWithRhino)
    }

    it("Should print a simple function call") {
      JsFunctionCall("foo", "a", "b").asString.parseWithRhino should equal("foo(a,b)".parseWithRhino)
    }

    it("Should print a call constructor call") {
      JsNew("foo", List("a", "b")).asString.parseWithRhino should equal("new foo(a,b)".parseWithRhino)
    }

    it("Should print a throw") {
      JsThrow("foo").asString.parseWithRhino should equal("""throw "foo"""".parseWithRhino)
    }

    it("Should print a try catch block") {
      JsTryCatch(JsNum(2), "e", JsNum(1)).asString.parseWithRhino should equal("try{2}catch(e){1}".parseWithRhino)
    }

    it("Should print a return with a value") {
      JsReturn(Some(JsNum(2))).asString.parseWithRhinoWrappedInFunction should equal("return 2".parseWithRhinoWrappedInFunction)
    }

    it("Should print a return without a value") {
      JsReturn(None).asString.parseWithRhinoWrappedInFunction should equal("return".parseWithRhinoWrappedInFunction)
    }

    it("Should print a forloop with break and continue") {
      val ini = JsDef("i", JsNum(0))
      val con = JsBinOp("i", "<", JsNum(23))
      val inc = JsUnOp("++", "i")
      val b = JsContinue & JsBreak
      JsFor(ini, con, inc, b).asString.parseWithRhino should equal("for(var i=0;(i<23);(++i)){continue;break;}".parseWithRhino)
    }

    it("Should print a foreach loop") {
      JsForIn("x", "c", JsNull).asString.parseWithRhino should equal("for(x in c){null}".parseWithRhino)
    }

    it("Should print a while loop") {
      JsWhile("x", JsNull).asString.parseWithRhino should equal("while(x){null}".parseWithRhino)
    }
    it("Should print a dowhile loop") {
      JsDoWhile("x", JsNull).asString.parseWithRhino should equal("do{null}while(x);".parseWithRhino)
    }

    it("Should print if a without else") {
      JsIf("x", JsNull).asString.parseWithRhino should equal("if(x){null}".parseWithRhino)
    }
    it("Should print if a with else") {
      JsIfElse("x", JsNull, JsNull).asString.parseWithRhino should equal("if(x){null}else{null}".parseWithRhino)
    }

    it("Should print three noops separate with semicolon") {
      (Noop & Noop & Noop).asString.parseWithRhino should equal(";;".parseWithRhino)
    }

    it("Should print a simple assigment") {
      JsAssignment("x", "y").asString.parseWithRhino should equal("x=y".parseWithRhino)
    }

    it("Should print a simple definition") {
      JsDef("x", "y").asString.parseWithRhino should equal("var x=y".parseWithRhino)
    }
    it("Should print a simple declaration") {
      JsDeclaration("x").asString.parseWithRhino should equal("var x".parseWithRhino)
    }

    it("Should print a simple function definition") {

      JsFunction("foo", List("a", "b"), JsReturn(Some("a"))).asString.parseWithRhino should equal("function foo(a,b){return a}".parseWithRhino)
    }
  }
  
  describe(testedImplementationName() + " Test case 2: Advanced  tests") {
    it("Should print a factorial function ") {
      JsFunction("fac", List("n"),
        JsIf(JsBinOp("n", "<=", JsNum(1)), JsReturn(Some(JsNum(1)))) &
          JsReturn(Some(JsBinOp("n", "*",
            JsFunctionCall("fac", JsBinOp("n", "-", JsNum(1))))))).asString.parseWithRhino should equal(
          "function fac(n){\nif((n<=1)) {return 1};\nreturn (n*fac((n-1)))}".parseWithRhino)

    }
  }
}
