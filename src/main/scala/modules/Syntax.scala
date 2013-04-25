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
 * Abstract syntax of SL
 */
trait Syntax {

  /**
   * Simple data structure representing the position (line and column) in a source file.
   */
  case class Position(line: Int, col: Int)

  /**
   * A location represents the area in a source file where a syntactical
   * element is defined. The empty location is useful when writing syntax trees
   * manually.
   */
  sealed abstract class Location
  case class FileLocation(file: String, from: Position, to: Position) extends Location
  case object NoLocation extends Location

  /**
   * Each node in the syntax tree carries a set of attributes around. Right
   * now, these are only source code locations. The empty attribute is useful
   * when writing syntax trees manually.
   */
  sealed abstract class Attribute {
    /*
     * This little hack allows us to compare AST elements without taking the
     * attributes into account. If attributes matter, we have to access them
     * explicitely
     */
    override def hashCode(): Int = { classOf[Attribute].hashCode() }
    override def equals(other: Any) = other match {
      case a: Attribute => true
      case _ => false
    }
  }
  case class AttributeImpl(location: Location) extends Attribute
  case object EmptyAttribute extends Attribute

  /*
   * All identifiers are currently represented by strings (since we do not
   * use a symbol table).
   */

  // TODO: Rename those types such that it's clear that they represent the types for certain kinds of identifiers
  type Var = String

  type TypeVar = String

  type ConVar = String

  type TConVar = String


  /**
   *
   */
  sealed abstract class AST {
    override def toString(): String = ASTPrettyPrinter.pretty(this)
  }

  /**
   * A program consists of top level definitions which might be
   * annotated with an explicit type signature.
   */
  case class Program(signatures: Map[Var, FunctionSig], functionDefs: Map[Var, List[FunctionDef]],
    dataDefs: List[DataDef], attribute: Attribute = EmptyAttribute) extends AST

  /**
   * Type signature for top-level function definitions.
   */
  case class FunctionSig(typ: ASTType, attribute: Attribute = EmptyAttribute)

  /**
   *  Top-level function definitions.
   */
  case class FunctionDef(patterns: List[Pattern], expr: Expr, attribute: Attribute = EmptyAttribute)

  /**
   * Patterns for top-level function definitions.
   */
  sealed abstract class Pattern
  case class PatternVar(ide: Var, attribute: Attribute = EmptyAttribute) extends Pattern
  case class PatternExpr(con: ConVar, patExprs: List[Pattern], attribute: Attribute = EmptyAttribute) extends Pattern

  def varsList(p: Pattern): List[Var] = p match {
    case PatternVar(x, _)       => List(x)
    case PatternExpr(_, sub, _) => sub.flatMap(vars)
  }

  def vars(p: Pattern): Set[Var] = varsList(p).toSet

  /**
   * Data type definitions.
   */
  case class DataDef(ide: TConVar, tvars: List[TypeVar], constructors: List[ConstructorDef], attribute: Attribute = EmptyAttribute)

  /**
   * A datatype consists of a list of data constructors CondDef and
   * the types of the arguments they take.
   */
  case class ConstructorDef(constructor: ConVar, types: List[ASTType], attribute: Attribute = EmptyAttribute)

  /**
    * All type constructors of a program.
    */
  def allTypeCons(dataDefs: List[DataDef]): List[TConVar] = dataDefs map (_.ide)

  /**
    * All data constrcutor of a program.
    */
  def allDataCons(dataDefs: List[DataDef]): List[ConVar] = dataDefs.flatMap(_.constructors).map(_.constructor)

  /**
    * All applications of type constructors in type definitions.
    */
  def allAppTypeCons(conDefs: List[ConstructorDef]): List[TConVar] = {
    val conTypes: List[ASTType] = conDefs flatMap (_.types)

    def selectAppTypeCon(ty: ASTType) = ty match {
      case TyExpr(conType, _, _) => List(conType)
      case _                     => Nil
    }

    conTypes flatMap selectAppTypeCon
  }

  /**
    * Abstract syntax for types.
    */
  sealed abstract class ASTType {
    
    /**
      * Arity of this type.
      */
    def arity: Int = this match {
      case FunTy(types, _) => types.length - 1
      case _               => 0
    }
  }

  case class TyVar(ide: TypeVar, attribute: Attribute = EmptyAttribute) extends ASTType
  case class FunTy(types: List[ASTType], attribute: Attribute = EmptyAttribute) extends ASTType
  case class TyExpr(conType: TConVar, typeParams: List[ASTType], attribute: Attribute = EmptyAttribute) extends ASTType


  /**
    * Right-hand sides of top-level definitions consist of expressions.
    */
  sealed abstract class Expr {
    override def toString(): String = ASTPrettyPrinter.pretty(this)
  }
  case class Conditional(condition: Expr, thenE: Expr, elseE: Expr, attribute: Attribute = EmptyAttribute) extends Expr
  case class Lambda(patterns: List[Pattern], expr: Expr, attribute: Attribute = EmptyAttribute) extends Expr
  case class Case(expr: Expr, alternatives: List[Alternative], attribute: Attribute = EmptyAttribute) extends Expr
  case class Let(definitions: List[LetDef], body: Expr, attribute: Attribute = EmptyAttribute) extends Expr
  case class App(function: Expr, expr: Expr, attribute: Attribute = EmptyAttribute) extends Expr
  case class ExVar(ide: Var, attribute: Attribute = EmptyAttribute) extends Expr
  case class ExCon(con: ConVar, attribute: Attribute = EmptyAttribute) extends Expr
  case class ConstInt(value: Int, attribute: Attribute = EmptyAttribute) extends Expr
  case class ConstChar(value: Char, attribute: Attribute = EmptyAttribute) extends Expr
  case class ConstString(value: String, attribute: Attribute = EmptyAttribute) extends Expr
  case class ConstReal(value : Double, attribute: Attribute = EmptyAttribute) extends Expr
  case class JavaScript(jsCode: String, signature: Option[ASTType], attribute: Attribute = EmptyAttribute) extends Expr

  /**
    * Local definition in a let-binding.
    */
  case class LetDef(lhs: Var, rhs: Expr, attribute: Attribute = EmptyAttribute)

  /**
    * Alternative in a case expression.
    */
  case class Alternative(pattern: Pattern, expr: Expr, attribute: Attribute = EmptyAttribute)

  /**
    * Select the attribute of an expression.
    */
  def attribute(expr: Expr): Attribute = expr match {
    case Conditional(_, _, _, attr) => attr
    case Lambda(_, _, attr) => attr
    case Case(_, _, attr) => attr
    case Let(_, _, attr) => attr
    case App(_, _, attr) => attr
    case ExVar(_, attr) => attr
    case ExCon(_, attr) => attr
    case ConstInt(_, attr) => attr
    case ConstChar(_, attr) => attr
    case ConstString(_, attr) => attr
    case JavaScript(_, _, attr) => attr
  }

  //TODO: test!
  def fv(fd : FunctionDef) : Set[Var] = {
    fv(fd.expr) -- (for (p <- fd.patterns; v <- vars(p)) yield v)
  }

  def fv(expr : Expr) : Set[Var] = expr match {
    case ExCon(_, _) => Set()
    case ConstInt(_, _) => Set()
    case ConstChar(_, _) => Set()
    case ConstString(_, _) => Set()
    case ConstReal(_, _) => Set()
    case JavaScript(_, _, _) => Set()
    case ExVar(x, _) => Set(x)
    case App(l, r, _) => fv(l) ++ fv(r)
    case Let(defs, rhs, _) => fv(rhs) ++ (for(d <- defs; v <- fv(d.rhs)) yield v) -- defs.map(_.lhs)
    case Conditional(c, t, e, _) => fv(c) ++ fv(t) ++ fv(e)
    case Lambda(p, rhs, _) => fv(rhs) -- (for(pat <- p ; v <- vars(pat)) yield v)
    case Case(e, a, _) => fv(e) ++ (for(alt <- a; v <- fv(alt.expr)) yield v) -- (for(alt <- a ; v <- vars(alt.pattern)) yield v)
  }
  

  /**
    * Pretty printer for SL definitions and expressions.
    */
  object ASTPrettyPrinter extends org.kiama.output.PrettyPrinter with Lexic {

    def pretty(t: Any): String = t match {
      case e: Expr => super.pretty(showExpr(e))
      case m: Program => super.pretty(showProgram(m))
      case e => pretty_any(e)
    }

    def showProgram(m: Program): Doc = {
      var doc = line

      for (d <- m.dataDefs)
        doc = doc <@> showDataDef(d)

      for ((name, s) <- m.signatures)
        doc = doc <@> funLex <+> name <+> typeLex <+> showType(s.typ) <> line

      for ((name, ds) <- m.functionDefs ;
	   d <- ds) {
	doc = doc <@> defLex <+> name <+> catList(d.patterns.map(showPattern), "") <+> funEqLex <+> showExpr(d.expr) <> line
      }
      doc
    }

    def showDataDef(d : DataDef) : Doc = d match {
      case DataDef(name, Nil, cons, _) => {
        dataLex <+> d.ide <+> funEqLex <+> catList(d.constructors map showConstructor, space <> dataSepLex) <> line
      }
      case DataDef(name, vars, cons, _) => {
        dataLex <+> d.ide <+> hsep(d.tvars.map(value)) <+> funEqLex <+> catList(d.constructors map showConstructor, space <> dataSepLex) <> line
      }
    }

    def showConstructor(c: ConstructorDef) = c.constructor <+> hsep(c.types.map(showType))

    def showType(t: ASTType): Doc = t match {
      case TyVar(i, a) => i
      case TyExpr(c, Nil, _) => c
      case TyExpr(c, ts, a) => parens(c <+> hsep(ts.map(showType)))
      case FunTy(ps, a) => catList(ps map showType, arrowLex)
    }

    def showExpr(t: Expr): Doc = t match {
      case Conditional(c, t, e, a) => ifLex <+> showExpr(c) <@> thenLex <+> nest(line <> showExpr(t)) <@> elseLex <> nest(line <> showExpr(e))
      case Lambda(ps, e, a) => parens(lambdaLex <+> list(ps, "", showPattern _, "") <+> dotLex <> nest(line <> showExpr(e)))
      case Case(e, as, a) => caseLex <+> showExpr(e) <@> ssep(as.map(showAlt), linebreak)
      case Let(ds, e, a) => letLex <+> nest(line <> cat(ds.map(showLefDef))) <@> inLex <> nest(line <> showExpr(e))
      case App(f, e, a) => parens(showExpr(f) <+> showExpr(e))
      case ExVar(i, a) => i
      case ExCon(c, a) => c
      case ConstInt(v, a) => value(v)
      case ConstChar(c, a) => dquotes(value(c))
      case ConstString(s, a) => dquotes(value(s))
      case ConstReal(x, _) => x.toString
      case JavaScript(j, s, a) => {
	val sigDoc = s match {
	  case None      => empty
	  case Some(sig) => " :" <+> sig.toString
	}
	jsOpenLex <+> j <+> jsCloseLex <> sigDoc
      }
    }

    def showLefDef(l: LetDef): Doc = l.lhs <+> funEqLex <+> showExpr(l.rhs)
    def showAlt(a: Alternative): Doc = ofLex <+> showPattern(a.pattern) <+> thenLex <+> nest(showExpr(a.expr))
    def showPattern(p: Pattern): Doc = p match {
      case PatternVar(v, a) => v
      case PatternExpr(c, ps, a) => c <+> catList(ps.map(showPattern), "")
    }

    def catList(l: List[Doc], sep: Doc): Doc = (group(nest(lsep(l, sep))))
  }

  
}


/**
  * The type aliases are needed at some points for instantiating traits,
  * thus we need a companion object to be able to import these type values.
  */
object Syntax {
  type Var = String

  type TypeVar = String

  type ConVar = String

  type TConVar = String
}
