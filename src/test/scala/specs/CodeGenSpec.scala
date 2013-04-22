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
import org.mozilla.javascript.{ Context, ScriptableObject, Function, Scriptable }
import org.mozilla.javascript.commonjs.module._
import org.mozilla.javascript.commonjs.module.provider._
import de.tuberlin.uebb.sl2.modules._
import java.io.{ InputStreamReader, BufferedReader }
import java.lang.ClassLoader
import java.net._
import scala.collection.JavaConversions._
import scala.io.Source

trait CodeGenSpec extends FunSpec with Inside with ShouldMatchers with SLPrograms {

  this: Syntax with Parser with JsSyntax with CodeGenerator =>

  def getClassPathUrls(cl: ClassLoader): List[URI] = cl match {
    case ucl: URLClassLoader => {
      getClassPathUrls(cl.getParent) ++ (ucl.getURLs map (_.toURI))
    }
    case null => List()
    case _ => getClassPathUrls(cl.getParent)
  }

  val paths = getClassPathUrls(this.getClass.getClassLoader)
  val sourceProvider = new UrlModuleSourceProvider(paths, null)
  val scriptProvider = new SoftCachingModuleScriptProvider(sourceProvider)
  val builder = new RequireBuilder()
  builder.setModuleScriptProvider(scriptProvider)

  def getJsScope(cx: Context) = {
    val scope = cx.initStandardObjects()
    val require = builder.createRequire(cx, scope)
    require.install(scope)

    val argsObj = cx.newArray(scope, new Array[AnyRef](0))
    scope.defineProperty("arguments", argsObj, ScriptableObject.DONTENUM)
    scope
  }

  val preludeSl = Source.fromURL(getClass.getResource("/prelude.sl")).getLines.mkString("\n")
  val preludeJs = Source.fromURL(getClass.getResource("/prelude.js")).getLines.mkString("\n")

  lazy val exprPrelude = {
    val ast = parseAst(preludeSl).right.get
    val astJs = astToJs(ast)
    JsPrettyPrinter.pretty(astJs) + "\n" + preludeJs
  }

  implicit class jsString(val str: String) {

    def evaluated() : String = {
      val cx = Context.enter()
      val res = try {
        val scope = getJsScope(cx)
        val result = cx.evaluateString(scope, str, "<evaluating>", 1, null)
        Context.toString(result)
      } finally {
	// TODO: Produces compiler warning
        Context.exit()
      }

      res
    }
  }

  implicit class slString(val str: String) {

    def compiled() = {
      val exp = parseExpr(str).right.get
      val js = expToJs(exp, "tmp") & JsName("tmp")
      "/*import code*/\n%s\n/*test code*/\n%s".format(exprPrelude, JsPrettyPrinter.pretty(js))
    }

    def compileProgramAndExpr(expr: String) = {
//            println(str+"\n"+parseAst(preludeSl + str))
      val ast = parseAst(preludeSl + str).right.get

      val astJs = astToJs(ast)
      val exp = parseExpr(expr).right.get
      val js = expToJs(exp, "tmp") & JsName("tmp")
//      println("/*import code*/\n%s\n/*program code*/\n%s\n/*test code*/\n%s".format(
//        preludeJs, JsPrettyPrinter.pretty(astJs), JsPrettyPrinter.pretty(js)))
      "/*import code*/\n%s\n/*program code*/\n%s\n/*test code*/\n%s".format(
        preludeJs, JsPrettyPrinter.pretty(astJs), JsPrettyPrinter.pretty(js))
    }
  }


  describe("Compiling simple SL expressions") {

    it("Should compile the 'True' literal correctly") {
      ("True".compiled.evaluated) should equal("true".evaluated)
    }

    it("Should compile the 'False' literal correctly") {
      ("False".compiled.evaluated) should equal("false".evaluated)
    }

    it("Should compile integer literals correctly") {
      ("42".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile character literals correctly") {
      ("'c'".compiled.evaluated) should equal("'c'".evaluated)
    }

    it("Should compile string literals correctly") {
      (""""42"""".compiled.evaluated) should equal(""""42"""".evaluated)
    }

    it("Should compile addition correctly") {
      ("40 + 2".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile string concatenation correctly") {
      (""""a" +s "b"""".compiled.evaluated) should equal(""" "ab" """.evaluated)
    }

    it("Should compile multiplication correctly") {
      ("40 * 2".compiled.evaluated) should equal("80".evaluated)
    }

    it("Should compile division correctly") {
      ("40 / 2".compiled.evaluated) should equal("20".evaluated)
    }

    it("Should compile subtraction correctly") {
      ("44 - 2".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile if-then-else correctly") {
      ("IF True THEN 42 ELSE 23".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile the identity lambda expression correctly") {
      ("(\\ x . x) 42".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile lambdas with multiple arguments correctly") {
      "(\\x y. x) 1 2".compiled.evaluated should equal("1".evaluated)
    }
  }


  describe("Compiling function definitions") {

    it("Should compile the identity function correctly") {
      ("DEF id x = x".compileProgramAndExpr("id 42").evaluated) should equal("42".evaluated)
    }

    it("Should compile the factorial function correctly") {
      ("DEF fac n = IF n == 1 THEN 1 ELSE n * fac (n - 1)".compileProgramAndExpr("fac 13").evaluated) should equal("6227020800".evaluated)
    }

    it("Should compile the head and tail function correctly") {
      ("""DEF head (Cons x xs) = x
          DEF tail (Cons x xs) = xs""".compileProgramAndExpr("head (tail (Cons 2 (Cons 1 Nil)))").evaluated) should equal("1".evaluated)
    }

    it("Should compile the tree data structure correctly") {
      ("""DATA Tree a = Leaf a | Node (Tree a) (Tree a)
          DEF sum (Leaf n) = n
          DEF sum (Node x y) = sum x + sum y""".compileProgramAndExpr("sum (Node (Leaf 13) (Node (Leaf 2)(Leaf 3)))").evaluated) should equal("18".evaluated)
    }

    it("Should compile case") {
      ("""DATA Tree a = Leaf a | Node (Tree a) (Tree a)
          DEF sum x = CASE x
                      OF Leaf a THEN a
                      OF Node x y THEN sum x + sum y""".compileProgramAndExpr("sum (Node (Leaf 13) (Node (Leaf 2)(Leaf 3)))").evaluated) should equal("18".evaluated)
    }


    it("Should compile mutually recursive even and odd function") {
      ("""DEF even n = IF n == 0 THEN True ELSE odd (n-1)  
          DEF odd n = IF n == 1 THEN True ELSE even (n-1) """.compileProgramAndExpr("even 22").evaluated) should equal("true".evaluated)
    }

    it("Should compile mutually recursive even and odd in let expression") {
      ("""LET even = \ n . IF n == 0 THEN True ELSE odd (n-1)  
              odd = \ n . IF n == 1 THEN True ELSE even (n-1)  
              IN even 22 """.compiled.evaluated) should equal("true".evaluated)
    }

    it("Should compile nested lets") {
      ("""LET a = LET c = 3 IN c + c IN a""".compiled.evaluated) should equal("6".evaluated)
    }


    it("Should successfully compile functions with many parameters") {
      multipleParams.compileProgramAndExpr("""add 1 2 3""").evaluated should equal("6".compiled.evaluated)
    }
  }


  describe("Compiling constants") {
    it("Evaluate the declarations in order of dependency") {
      constants.compileProgramAndExpr("""c3""").evaluated should equal("2".compiled.evaluated)
    }

    it("Should hide constants by LETs") {
      constants.compileProgramAndExpr("""l3""").evaluated should equal("3".compiled.evaluated)
    }

    it("Should correctly evaluate shadowed right hand sides") {
      constants.compileProgramAndExpr("""l1""").evaluated should equal("1".compiled.evaluated)
    }

    it("Should correctly evaluate shadowed calculating right hand sides") {
      constants.compileProgramAndExpr("""l2""").evaluated should equal("2".compiled.evaluated)
    }

    it("Should correctly capture closure variables") {
      constants.compileProgramAndExpr("""l4""").evaluated should equal("8".compiled.evaluated)
    }
  }


  describe("Compiling functions using pattern matching") {
    it("Should compile late matches") {
      lateMatch.compileProgramAndExpr("f (Cons 1 Nil) 3 Nil").evaluated should equal("3".evaluated)
    }

    it("Should compile nested matches") {
      "DEF f (Cons (Cons a b) (Cons d Nil)) = a".compileProgramAndExpr("f (Cons (Cons 4 (Cons 5 Nil)) (Cons (Cons 8 Nil) Nil))").evaluated should equal("4".evaluated)
    }

    it("Should compile mixed patterns") {
      mixedPatterns.compileProgramAndExpr("f Green True").evaluated should equal("2".evaluated)
    }

    it("Should compile function with overlapping patterns") {
      overlappingPatterns.compileProgramAndExpr("f 1 2 3").evaluated should equal("1".evaluated)
    }
  }
  

  describe("Compiling list concatenation") {
    it("Should work on the empty lists") {
      concat.compileProgramAndExpr("""Nil ++ Nil""").evaluated should equal("Nil".compiled.evaluated)
    }

    it("Should work on the singleton list") {
      concat.compileProgramAndExpr("""(Cons 1 Nil) ++ Nil""").evaluated should equal("Cons 1 Nil".compiled.evaluated)
    }

    it("Should be symmetric on the empty list") {
      concat.compileProgramAndExpr("""Nil ++ (Cons 1 Nil)""").evaluated should equal("Cons 1 Nil".compiled.evaluated)
    }

    it("Should append to the end of the list") {
      concat.compileProgramAndExpr("""(Cons 2 Nil) ++ (Cons 1 Nil)""").evaluated should equal("Cons 2 (Cons 1 Nil)".compiled.evaluated)
    }

    it("Should work on slightly larger lists") {
      concat.compileProgramAndExpr("""(Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil)))) ++ (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))""").evaluated should equal("""(Cons 4 (Cons 3 (Cons 2 (Cons 1 (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))))))""".compiled.evaluated)
    }
  }


  describe("Compiling a list generator") {
    it("Should work for the recursion anchor") {
      range.compileProgramAndExpr("""range 0""").evaluated should be ("Cons 0 Nil".compiled.evaluated)
    }

    it("Should work for a smaller list") {
      range.compileProgramAndExpr("""range 4""").evaluated should be ("""Cons 4 (Cons 3 (Cons 2 (Cons 1 (Cons 0 Nil))))""".compiled.evaluated)
    }
  }

  
  describe("Compiling filter") {
    it("Should work in the empty list") {
      filter.compileProgramAndExpr("""filter Nil (\ x . False)""").evaluated should equal("Nil".compiled.evaluated)
    }
    
    it("Should work on equality") {
      filter.compileProgramAndExpr("""filter (Cons 1 (Cons 2 (Cons 0 Nil))) (\ x . x == 0)""").evaluated should equal("Cons 0 Nil".compiled.evaluated)
    }

    it("Should work on greater-than") {
      filter.compileProgramAndExpr("""filter (Cons 1 (Cons 0 (Cons 2 Nil))) (\ x . x > 0)""").evaluated should equal("Cons 1 (Cons 2 Nil)".compiled.evaluated)
    }
  }


  describe("Compiling reverse") {
    it("Should work on the empty list") {
      reverse.compileProgramAndExpr("reverse Nil").evaluated should equal("Nil".compiled.evaluated)
    }
    
    it("Should work on the singleton list") {
      reverse.compileProgramAndExpr("reverse (Cons 1 Nil)").evaluated should equal("Cons 1 Nil".compiled.evaluated)
    }

    it("Should work on a 4-element list") {
      reverse.compileProgramAndExpr("reverse (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))").evaluated should equal("Cons 1 (Cons 2 (Cons 3 (Cons 4 Nil)))".compiled.evaluated)
    }

    it("Should work be it's own inverse") {
      reverse.compileProgramAndExpr("reverse (reverse (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil)))))").evaluated should equal("Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil)))".compiled.evaluated)
    }
  }


  describe("Compiling quicksort") {
    it("Should work in the empty list") {
      sort.compileProgramAndExpr("""quicksort Nil""").evaluated should equal("Nil".compiled.evaluated)
    }

    it("Should work on the singleton list") {
      sort.compileProgramAndExpr("""quicksort (Cons 1 Nil)""").evaluated should equal("Cons 1 Nil".compiled.evaluated)
    }

    it("Should work on an ordered list") {
      sort.compileProgramAndExpr("""quicksort (Cons 1 (Cons 2 (Cons 3 (Cons 4 Nil))))""").evaluated should equal("Cons 1 (Cons 2 (Cons 3 (Cons 4 Nil)))".compiled.evaluated)
    }
    
    it("Should work on an reverse-ordered list") {
      sort.compileProgramAndExpr("""quicksort (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))""").evaluated should equal("Cons 1 (Cons 2 (Cons 3 (Cons 4 Nil)))".compiled.evaluated)
    }

    it("Should work on larger input") {
      sort.compileProgramAndExpr("quicksort (reverse (range 100))").evaluated should equal(range.compileProgramAndExpr("range 100").evaluated)
    }

    it("Should work on even larger input") {
      sort.compileProgramAndExpr("quicksort (reverse (range 1000))").evaluated should equal(range.compileProgramAndExpr("range 1000").evaluated)
    }
  }


  describe("Compiling SL programs") {
    it("Should compile a function with `Bool' arguments") {
      ("""DEF f True  x = 0
          DEF f False x = 1""".compileProgramAndExpr("f True 10").evaluated) should equal("0".evaluated)
    }

    it("Should compile a case expressions on a built-in type") {
      caseWithBuiltIn.compileProgramAndExpr("f False").evaluated should equal("2".evaluated)
    }

    it("Should compile a case expressions on a user-defined type") {
      caseWithCustomType.compileProgramAndExpr("f True 251").evaluated should equal("251".evaluated)
    }

    it("Should compile nested conditionals") {
      nestedConditional.compileProgramAndExpr("f 100 200 300").evaluated should equal("0".evaluated)
    }

    it("Should compile a function with a reserved JavaScript name as argument name") {
      ("""DEF f return = return + 1""".compileProgramAndExpr("f 1").evaluated) should equal("2".evaluated)
    }

    it("Should compile pattern matching in lambda abstractions") {
      lambdaPatterns.compileProgramAndExpr("f").evaluated should equal("-531".evaluated)
    }

    it("Should compile functions with shadowed local definitions") {
      shadowedLocalDef.compileProgramAndExpr("f").evaluated should equal("0".evaluated)
    }

    it("Should compile functions with nested local definitions") {
      nestedLet.compileProgramAndExpr("result").evaluated should equal("12345".evaluated)
    }

    it("Should compile functions with shadowed pattern variables") {
      shadowedPatternVar.compileProgramAndExpr("f (Cons 1 (Cons 2 Nil)) 10").evaluated should equal("11".evaluated)
    }

    it("Should compile functions where pattern variables shadow top-level names") {
      shadowedTopLevelNames.compileProgramAndExpr("f (-5) (-10) 0").evaluated should equal("-15".evaluated)
    }

    it("Should compile ulam function") {
      ulam.compileProgramAndExpr("ulam 11").evaluated should equal("1".evaluated)
    }

    it("Should compile partial application") {
      partialApplication.compileProgramAndExpr("call (-10) (g 10)").evaluated should equal("0".evaluated)
    }

    it("Should compile functions with shadowed local names") {
      shadowedVars.compileProgramAndExpr("f").evaluated should equal("-590".evaluated)
    }
  }

}
