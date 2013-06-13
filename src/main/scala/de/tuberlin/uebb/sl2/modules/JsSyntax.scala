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

package de.tuberlin.uebb.sl2.modules

import org.kiama.output.PrettyPrinter
import scala.language.implicitConversions

/**
  * Abstract syntax of JavaScript
  */
trait JsSyntax {

  sealed abstract class JsStmt {
    def &(other: JsStmt) = JsStmtConcat(List(this, other))
  }

  case class JsStmtConcat(stmts: List[JsStmt]) extends JsStmt {
    override def &(other: JsStmt) = other match {
      case JsStmtConcat(l) => JsStmtConcat(stmts ++ l)
      case _ => JsStmtConcat(stmts :+ other)
    }
  }

  case class JsFunction(name: JsName, params: List[JsName], body: JsStmt) extends JsStmt
  // var name;
  case class JsDeclaration(name: JsName) extends JsStmt
  // lhs = rhs
  case class JsAssignment(lhs: JsExpr, rhs: JsExpr) extends JsStmt
  // var name = rhs;
  case class JsDef(name: JsName, rhs: JsExpr) extends JsStmt
  object Noop extends JsStmt

  case class JsIf(conditiond: JsExpr, body: JsStmt) extends JsStmt
  case class JsIfElse(conditiond: JsExpr, body: JsStmt, elseBody: JsStmt) extends JsStmt
  case class JsWhile(conditiond: JsExpr, body: JsStmt) extends JsStmt
  case class JsDoWhile(conditiond: JsExpr, body: JsStmt) extends JsStmt
  case class JsFor(initial: JsStmt, condition: JsStmt, increment: JsStmt, body: JsStmt) extends JsStmt
  //TODO check if JsStmt is in JsForIn really necessary
  case class JsForIn(x: JsStmt, in: JsStmt, body: JsStmt) extends JsStmt
  object JsBreak extends JsStmt
  object JsContinue extends JsStmt
  case class JsReturn(e: Option[JsExpr]) extends JsStmt {
    def this() = this(None)
    def this(e: JsExpr) = this(Some(e))
  }
  case class JsBlock(body: JsStmt) extends JsStmt
  case class JsTryCatch(tryBody: JsStmt, catchVar: JsName, catchBody: JsStmt) extends JsStmt
  case class JsThrow(exception: String) extends JsStmt

  sealed abstract class JsExpr extends JsStmt
  case class JsName(name: String) extends JsExpr

  case class JsNum(n: Number) extends JsExpr
  case class JsStr(s: String) extends JsExpr
  case class JsBool(b: Boolean) extends JsExpr
  case class JsRaw(s: String) extends JsExpr
  object JsNull extends JsExpr
  case class JsNew(name: JsName, args: List[JsExpr]) extends JsExpr
  // Foo(x,y,z)
  case class JsFunctionCall(function: JsExpr, args: JsExpr*) extends JsExpr
  // obj[arg]
  case class JsMemberAccess(obj: JsExpr, arg: JsExpr) extends JsExpr
  //{s1:e1, s2:e1,..., sn:en}
  case class JsObject(fields: List[(JsName, JsExpr)]) extends JsExpr

  case class JsAnonymousFunction(params: List[JsName], body: JsStmt) extends JsExpr

  case class JsBinOp(lhs: JsExpr, op: JsName, rhs: JsExpr) extends JsExpr
  case class JsUnOp(op: JsName, e: JsExpr) extends JsExpr

  sealed case class JsPattern(condition : JsExpr, variables : List[JsDef]) {
    def &(that : JsPattern) : JsPattern = {
      JsPattern(JsBinOp(condition, "&&", that.condition), variables ++ that.variables)
    }
  }

  implicit def nameToJsExpr(s: String): JsName = JsName(s)
  implicit def numToJsExpr(n: Number): JsNum = JsNum(n)
  implicit def boolToJsExpr(b: Boolean): JsBool = JsBool(b)
  implicit def stmtsToStmt(l: List[JsStmt]): JsStmtConcat = JsStmtConcat(l)

  object JsPrettyPrinter {
    import scala.text.Document
    import scala.text.Document._

    def pretty(js: JsStmt) = {
      val doc = show(js)
      val w = new java.io.StringWriter()
      doc.format(140, w)
      w.toString()
    }
    
    implicit def str2Doc(s : String) = text(s)

    def sepred(d : Seq[Document], sep : Document) : Document = {
      if (d.isEmpty)
        empty
      else if (d.size == 1)
        d.head
      else
        d.head :: sep :: sepred(d.tail, sep)
    }

    def parens(d : Document) = "(" :: d :: ")"
    
    def braces(d : Document) = "{" :: d :: "}"

    def brackets(d : Document) = "[" :: d :: "]"

    def nested(d : Document) : Document = nest(2, d)

    implicit def show(stmt: JsStmt) : Document = stmt match {
      case JsStmtConcat(l) =>  sepred(l map show, ";" :: break)
      case JsBlock(s) => showJsStmtAsBlock(s)
      case JsFunction(name, params, body) => "function" :: " " :: name :: parens(sepred(params map show, ", ")) :/: showJsStmtAsBlock(body)
      case JsDeclaration(n) => "var" :: " " :: n
      case JsDef(n,rhs) => "var " :: n :: " = " :: rhs //TODO rhs to long?
      case JsAssignment(l,r) => l :: " = " :: r
      case Noop => empty
      case JsIf(c,b) => "if"::parens(c):/:showJsStmtAsBlock(b)
      case JsIfElse(c,b,e) => "if" :: parens(c) :/: showJsStmtAsBlock(b) :/: "else " :/: showJsStmtAsBlock(e)
      case JsWhile(c,b)=>"while" :: parens(c) :/: showJsStmtAsBlock(b)
      case JsDoWhile(c,b)=>"do":/: showJsStmtAsBlock(b) :/: "while" :: parens(c)
      case JsFor(ini, c, inc, b)  => "for" :: parens(ini :: ";" :: c :: ";" :: inc) :/: showJsStmtAsBlock(b)
      case JsForIn(x, in,b) => "for" :: parens(x :: " in " :: in) :/: showJsStmtAsBlock(b)
      case JsBreak => "break"
      case JsContinue => "continue"
      case JsReturn(e) => "return " :: e.map(show).getOrElse(empty)
      case JsTryCatch(tb,c,cb) => "try" :/: showJsStmtAsBlock(tb) :/: "catch" :: parens(c) :/: showJsStmtAsBlock(cb)
      case JsThrow(s) => "throw ":: "\"" :: s :: "\""
      case JsName(n) => n
      case JsNum(v) => v.toString
      case JsBool(b) => b.toString
      case JsStr(s) => "\"" :: s :: "\""
      case JsRaw(s) =>  s
      case JsNew(n,args)  => "new " :: n :: parens(sepred(args.map(show), ", "))
      case JsFunctionCall(n,args @ _*)=> n :: parens(sepred(args.map(show), ", "))
      case JsMemberAccess(o,p) => o::brackets(p)
      case JsObject(f) => braces (nested (sepred(f.map(x =>x._1 :: " : " :: x._2), ", ")) :: break)
      case JsAnonymousFunction(params,b) => "function " :: parens(sepred(params map show, ", ")) :/: showJsStmtAsBlock(b)
      case JsBinOp(l,o,r) => parens(l :: " " :: o :: " " :: r)
      case JsUnOp(o,e) => parens(o :: e)
      case JsNull => "null"
    }
    
    
    def showJsStmtAsBlock(s: JsStmt) : Document = braces (nested (break :: s) :: break)
  }

}
