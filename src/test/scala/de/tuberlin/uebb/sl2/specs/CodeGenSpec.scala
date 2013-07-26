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
import org.scalatest.exceptions.TestFailedException
import org.mozilla.javascript.{ Context, ScriptableObject, Function, Scriptable }
import org.mozilla.javascript.commonjs.module._
import org.mozilla.javascript.commonjs.module.provider._
import de.tuberlin.uebb.sl2.impl._
import de.tuberlin.uebb.sl2.modules._
import java.io.{ BufferedReader, File, InputStreamReader }
import java.lang.ClassLoader
import java.net._
import scala.collection.JavaConversions._
import scala.io.Source

trait CodeGenSpec	
  extends FunSpec
  with Inside
  with ShouldMatchers
  with SLPrograms {

  this: Syntax
  with CodeGenerator
  with Configs
  with JsSyntax
  with Parser =>

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

  implicit class slString(val s: String) {

    def compiled() = {
      //val exp = parseExpr(str).right.get
      //val js = expToJs(exp, "tmp") & JsName("tmp")
      // TODO: add define() function
      val str = s.stripMargin
      val defineSrc = "var modules = {};\n" +
      		"var nextModule = '';\n" +
      		"var require = function(module) {\n" +
      		"   return modules[module]\n" +
      		"};\n" +
      		"define = function(fun) {\n" +
      		"   modules[nextModule] = {}\n" +
      		"   fun(require, modules[nextModule], null)\n" +
      		"};\n"
      // TODO: add compiled prelude
      val interlude1 = "nextModule=\"std/prelude.sl\";\n"
      //val prelude = "modules[nextModule] = {$True: true, $False : false};\n"
      val prelude = Source.fromFile(new File(getClass().getResource("/lib/prelude.sl.js").toURI())).getLines.mkString("\n")
      // TODO: add compiled source in main/template, invocation of main
      val conf = Main.defaultConfig
      // println("sl="+str)
      val js = Main.compileSL(str, conf)
      //for(jss <- "/*test code*/\n%s".format(defineSrc + interlude1 + prelude + interlude2 + js.right + testInvocation)) yield jss
      val interlude2 = "nextModule=\"test\";\n"
      /*val js = "define(function(require, exports, module) {"+
    "var $$std$prelude = require(\"std/prelude.sl\")"+
"; ; var $test = function () { var $test = true;"+
"	return $test }(); ; exports.$test = $test"+
"});"
      val testInvocation = "modules.test.$test"
	  println(defineSrc + interlude2 + js + testInvocation)*/
      val testInvocation = "modules.test.$test"
      //println(defineSrc + interlude1 + prelude + interlude2 + js.right.get + testInvocation)
      if(js.isLeft) {
    	  throw new TestFailedException("ERROR: "+js.left.get.toString+"while compiling: "+str+"\n", 0)
    	  "" // will lead to an EvaluatorException
      } else {
	      val result = (defineSrc + interlude1 + prelude + interlude2 + js.right.get + testInvocation + "\n")
	      //println(result)
	      result
      }
      /*"var modules = {};\n"+
"var nextModule = '';\n"+
"var require = function(module) {\n"+
"        return modules[module]\n"+
"};\n"+
"define = function(fun) {\n"+
"        modules[nextModule] = {}\n"+
"        fun(require, modules[nextModule], null)\n"+
"};\n"+
"nextModule=\"std/prelude.sl\";\n"+
"modules[nextModule] = {$True: true};\n"+
"nextModule=\"test\";\n"+
"define(function(require, exports, module) {\n"+
"    var $$std$prelude = require(\"std/prelude.sl\")\n"+
" \n"+
"; ; var $test = function () { var $test = $$std$prelude.$True; return $test }(); ; exports.$test = $test\n"+
"});modules.test.$test"*/
    }

    def compileProgramAndExpr(expr: String) = {
//            println(str+"\n"+parseAst(preludeSl + str))
      val ast = parseAst(s).right.get

      val astJs = astToJs(ast)
      val exp = parseExpr(expr).right.get
      val js = expToJs(exp, "tmp") & JsName("tmp")
//      println("/*import code*/\n%s\n/*program code*/\n%s\n/*test code*/\n%s".format(
//        preludeJs, JsPrettyPrinter.pretty(astJs), JsPrettyPrinter.pretty(js)))
      "/*program code*/\n%s\n/*test code*/\n%s".format(
        JsPrettyPrinter.pretty(astJs), JsPrettyPrinter.pretty(js))
    }
  }


  describe("Compiling simple SL expressions") {

    it("Should compile the 'True' literal correctly ") {
      ("""PUBLIC FUN test: Bool
         |DEF test = True""".compiled.evaluated) should equal("true".evaluated)
    }

    it("Should compile the 'False' literal correctly") {
      ("""PUBLIC FUN test: Bool
         |DEF test = False""".compiled.evaluated) should equal("false".evaluated)
    }

    it("Should compile integer literals correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = 42""".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile character literals correctly") {
      ("""PUBLIC FUN test: Char
      	 |DEF test = 'c'""".compiled.evaluated) should equal("'c'".evaluated)
    }

    it("Should compile string literals correctly") {
      ("""PUBLIC FUN test: String
         |DEF test = "42"""".compiled.evaluated) should equal(""""42"""".evaluated)
    }

    it("Should compile addition correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = 40 + 2""".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile string concatenation correctly") {
      ("""PUBLIC FUN test: String
         |DEF test = "a" ++ "b"""".compiled.evaluated) should equal(""" "ab" """.evaluated)
    }

    it("Should compile multiplication correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = 40 * 2""".compiled.evaluated) should equal("80".evaluated)
    }

    it("Should compile division correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = 40 / 2""".compiled.evaluated) should equal("20".evaluated)
    }

    it("Should round integer division") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = 5 / 2""".compiled.evaluated) should equal("2".evaluated)
    }

    it("Should compile subtraction correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = 44 - 2""".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile if-then-else correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = IF True THEN 42 ELSE 23""".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile the identity lambda expression correctly") {
      ("""PUBLIC FUN test: Int
      	 |DEF test = (\ x . x) 42""".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile lambdas with multiple arguments correctly") {
      ("""PUBLIC FUN test: Int
         |DEF test = (\x y. x) 1 2""".compiled.evaluated) should equal("1".evaluated)
    }
  }


  describe("Compiling function definitions") {

    it("Should compile the identity function correctly") {
      ("""DEF id x = x
      	 |PUBLIC FUN test: Int
      	 |DEF test = id 42""".compiled.evaluated) should equal("42".evaluated)
    }

    it("Should compile the factorial function correctly") {
      ("""DEF fac n = IF n == 1 THEN 1 ELSE n * fac (n - 1)
         |PUBLIC FUN test: Int
         |DEF test = fac 13""".compiled.evaluated) should equal("6227020800".evaluated)
    }

    it("Should compile the head and tail function correctly") {
      ("""DEF head (Cons x xs) = x
         |DEF tail (Cons x xs) = xs
         |PUBLIC FUN test: Int
         |DEF test = head (tail (Cons 2 (Cons 1 Nil)))""".compiled.evaluated) should equal("1".evaluated)
    }

    it("Should compile the tree data structure correctly") {
      ("""DATA Tree a = Leaf a | Node (Tree a) (Tree a)
         |DEF sum (Leaf n) = n
         |DEF sum (Node x y) = sum x + sum y
         |PUBLIC FUN test: Int
         |DEF test = sum (Node (Leaf 13) (Node (Leaf 2)(Leaf 3)))""".compiled.evaluated) should equal("18".evaluated)
    }

    it("Should compile case") {
      ("""DATA Tree a = Leaf a | Node (Tree a) (Tree a)
         |DEF sum x = CASE x
         |            OF Leaf a THEN a
         |            OF Node x y THEN sum x + sum y
         |PUBLIC FUN test: a
         |DEF test = sum (Node (Leaf 13) (Node (Leaf 2)(Leaf 3)))""".compiled.evaluated) should equal("18".evaluated)
    }


    it("Should compile mutually recursive even and odd function") {
      ("""DEF even n = IF n == 0 THEN True ELSE odd (n-1)  
         |DEF odd n = IF n == 1 THEN True ELSE even (n-1)
         |PUBLIC FUN test: a
         |DEF test = even 22""".compiled.evaluated) should equal("true".evaluated)
    }

    it("Should compile mutually recursive even and odd in let expression") {
      ("""PUBLIC FUN test: a
         |DEF test = LET even = \ n . IF n == 0 THEN True ELSE odd (n-1)  
         |    odd = \ n . IF n == 1 THEN True ELSE even (n-1)  
         |    IN even 22 """.compiled.evaluated) should equal("true".evaluated)
    }

    it("Should evaluate LETs in dependency-order") {
      """PUBLIC FUN test: a
        |DEF test = LET x=y y=5 IN x""".compiled.evaluated should equal("5".evaluated)
    }

    it("Should compile nested lets") {
      ("""PUBLIC FUN test: x
         |DEF test = LET a = LET c = 3 IN c + c IN a""".compiled.evaluated) should equal("6".evaluated)
    }


    it("Should successfully compile functions with many parameters") {
      ("""DEF add x y z = x + y + z
         |PUBLIC FUN test: a
      	 |DEF test = add 1 2 3""".compiled.evaluated) should equal("6".evaluated)
    }
  }


  describe("Compiling constants") {
    it("Evaluate the declarations in order of dependency") {
      (constants+"\n"+
       """PUBLIC FUN test: a
         |DEF test = c3""").compiled.evaluated should equal("2".evaluated)
    }

    it("Should hide constants by LETs") {
      (constants+"\n"+
       """PUBLIC FUN test: a
         |DEF test = l3""").compiled.evaluated should equal("3".evaluated)
    }

    it("Should correctly evaluate shadowed right hand sides") {
      (constants+"\n"+
       """PUBLIC FUN test: a
         |DEF test = l1""").compiled.evaluated should equal("1".evaluated)
    }

    it("Should correctly evaluate shadowed calculating right hand sides") {
      (constants+"\n"+
       """PUBLIC FUN test: a
         |DEF test = l2""").compiled.evaluated should equal("2".evaluated)
    }

    it("Should correctly capture closure variables") {
      (constants+"\n"+
       """PUBLIC FUN test: a
         |DEF test = l4""").compiled.evaluated should equal("8".evaluated)
    }
  }


  describe("Compiling functions using pattern matching") {
    it("Should compile late matches") {
      (lateMatch+"\n"+
       """PUBLIC FUN test: x
         |DEF test = f (Cons 1 Nil) 3 Nil""").compiled.evaluated should equal("3".evaluated)
    }

    it("Should compile nested matches") {
      """DEF f (Cons (Cons a b) (Cons d Nil)) = a
        |PUBLIC FUN test: x
        |DEF test = f (Cons (Cons 4 (Cons 5 Nil)) (Cons (Cons 8 Nil) Nil))""".compiled.evaluated should equal("4".evaluated)
    }

    it("Should compile mixed patterns") {
      (mixedPatterns+"\n"+
       """PUBLIC FUN test: x
         |DEF test = f Green True""").compiled.evaluated should equal("2".evaluated)
    }

    it("Should compile function with overlapping patterns") {
      (overlappingPatterns+"\n"+
       """PUBLIC FUN test: x
         |DEF test = f 1 2 3""").compiled.evaluated should equal("1".evaluated)
    }
  }
  

  describe("Compiling list concatenation") {
    it("Should work on the empty lists") {
      (concat+"\n"+
       """PUBLIC FUN test: a
         |DEF test = Nil +++ Nil""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
				 |DEF test = Nil""".compiled.evaluated)
    }

    it("Should work on the singleton list") {
      (concat+"\n"+
       """PUBLIC FUN test: a
         |DEF test = (Cons 1 Nil) +++ Nil""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
				 |DEF test = Cons 1 Nil""".compiled.evaluated)
    }

    it("Should be symmetric on the empty list") {
      (concat+"\n"+
       """PUBLIC FUN test: a
         |DEF test = Nil +++ (Cons 1 Nil)""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
        		 |DEF test = Cons 1 Nil""".compiled.evaluated)
    }

    it("Should append to the end of the list") {
      (concat+"\n"+
       """PUBLIC FUN test: a
         |DEF test = (Cons 2 Nil) +++ (Cons 1 Nil)""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
        		 |DEF test = Cons 2 (Cons 1 Nil)""".compiled.evaluated)
    }

    it("Should work on slightly larger lists") {
      (concat+"\n"+
       """PUBLIC FUN test: a
         |DEF test = (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil)))) +++ (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
        		 |DEF test = (Cons 4 (Cons 3 (Cons 2 (Cons 1 (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))))))""".compiled.evaluated)
    }
  }


  describe("Compiling a list generator") {
    it("Should work for the recursion anchor") {
      (range+"\n"+
       """PUBLIC FUN test: a
         |DEF test = range 0""").compiled.evaluated should
         be ("""PUBLIC FUN test: b
               |DEF test = Cons 0 Nil""".compiled.evaluated)
    }

    it("Should work for a smaller list") {
      (range+"\n"+
       """PUBLIC FUN test: a
         |DEF test = range 4""").compiled.evaluated should
         be ("""PUBLIC FUN test: b
               |DEF test = Cons 4 (Cons 3 (Cons 2 (Cons 1 (Cons 0 Nil))))""".compiled.evaluated)
    }
  }

  
  describe("Compiling filter") {
    it("Should work in the empty list") {
      (filter+"\n"+
       """PUBLIC FUN test: a
         |DEF test = filter Nil (\ x . False)""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
                 |DEF test = Nil""".compiled.evaluated)
    }
    
    it("Should work on equality") {
      (filter+"\n"+
       """PUBLIC FUN test: a
         |DEF test = filter (Cons 1 (Cons 2 (Cons 0 Nil))) (\ x . x == 0) """).compiled.evaluated should
         equal("""PUBLIC FUN test: b
                 |DEF test = Cons 0 Nil""".compiled.evaluated)
    }

    it("Should work on greater-than") {
      (filter+"\n"+
       """PUBLIC FUN test: a
         |DEF test = filter (Cons 1 (Cons 0 (Cons 2 Nil))) (\ x . x > 0)""").compiled.evaluated should
         equal("""PUBLIC FUN test: b
                 |DEF test = Cons 1 (Cons 2 Nil)""".compiled.evaluated)
    }
  }


  describe("Compiling reverse") {
    it("Should work on the empty list") {
      (reverse+"\n"+
       """PUBLIC FUN test: a
         |DEF test = reverse Nil""").compiled.evaluated should
         equal("""PUBLIC FUN test: a
                 |DEF test=Nil""".compiled.evaluated)
    }
    
    it("Should work on the singleton list") {
      (reverse+"\n"+
       """PUBLIC FUN test: a
         |DEF test = reverse (Cons 1 Nil)""").compiled.evaluated should
         equal("""PUBLIC FUN test: a
                 |DEF test=Cons 1 Nil""".compiled.evaluated)
    }

    it("Should work on a 4-element list") {
      (reverse+"\n"+
       """PUBLIC FUN test: a
         |DEF test = reverse (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil))))""").compiled.evaluated should
         equal("""PUBLIC FUN test: a
                 |DEF test=Cons 1 (Cons 2 (Cons 3 (Cons 4 Nil)))""".compiled.evaluated)
    }

    it("Should work be it's own inverse") {
      (reverse+"\n"+
       """PUBLIC FUN test: a
         |DEF test = reverse (reverse (Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil)))))""").compiled.evaluated should
         equal("""PUBLIC FUN test: a
                 |DEF test=Cons 4 (Cons 3 (Cons 2 (Cons 1 Nil)))""".compiled.evaluated)
    }
  }


  describe("Compiling quicksort") {
    it("Should work in the empty list") {
      (sort+"\n"+
       """PUBLIC FUN test: a
         |DEF test = quicksort Nil""").compiled.evaluated should
         equal("""PUBLIC FUN test: a
                 |DEF test=Nil""".compiled.evaluated)
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
         |DEF f False x = 1
         |PUBLIC FUN test: a
         |DEF test = f True 10""".compiled.evaluated) should equal("0".evaluated)
    }

    it("Should compile a case expressions on a built-in type") {
      (caseWithBuiltIn+"\n"+
       """PUBLIC FUN test: a
         |DEF test = f False""").compiled.evaluated should equal("2".evaluated)
    }

    it("Should compile a case expressions on a user-defined type") {
      (caseWithCustomType+"\n"+
       """PUBLIC FUN test: a
         |DEF test = f True 251""").compiled.evaluated should equal("251".evaluated)
    }

    it("Should compile nested conditionals") {
      (nestedConditional+"\n"+
       """PUBLIC FUN test: a
         |DEF test = f 100 200 300""").compiled.evaluated should equal("0".evaluated)
    }

    it("Should compile a function with a reserved JavaScript name as argument name") {
      ("""DEF f return = return + 1
         |PUBLIC FUN test: a
         |DEF test = f 1""".compiled.evaluated) should equal("2".evaluated)
    }

    it("Should compile pattern matching in lambda abstractions") {
      (lambdaPatterns+"\n"+
       """PUBLIC FUN test: r
         |DEF test = f""").compiled.evaluated should equal("-531".evaluated)
    }

    it("Should compile functions with shadowed local definitions") {
      (shadowedLocalDef+"\n"+
       """PUBLIC FUN test: r
         |DEF test = f""").compiled.evaluated should equal("0".evaluated)
    }

    it("Should compile functions with nested local definitions") {
      (nestedLet+"\n"+
       """PUBLIC FUN test: z
         |DEF test = result""").compiled.evaluated should equal("12345".evaluated)
    }

    it("Should compile functions with shadowed pattern variables") {
      (shadowedPatternVar+"\n"+
       """PUBLIC FUN test: z
         |DEF test = f (Cons 1 (Cons 2 Nil)) 10""").compiled.evaluated should equal("11".evaluated)
    }

    it("Should compile functions where pattern variables shadow top-level names") {
      (shadowedTopLevelNames+"\n"+
       """PUBLIC FUN test: z
         |DEF test = f (-5) (-10) 0""").compiled.evaluated should equal("-15".evaluated)
    }

    it("Should compile ulam function") {
      (ulam+"\n"+
       """PUBLIC FUN test: z
         |DEF test = ulam 11""").compiled.evaluated should equal("1".evaluated)
    }

    it("Should compile partial application") {
      (partialApplication+"\n"+
       """PUBLIC FUN test: z
         |DEF test = call (-10) (g 10)""").compiled.evaluated should equal("0".evaluated)
    }

    it("Should compile functions with shadowed local names") {
      (shadowedVars+"\n"+
       """PUBLIC FUN test: f
         |DEF test = f""").compiled.evaluated should equal("-590".evaluated)
    }
  }

}
