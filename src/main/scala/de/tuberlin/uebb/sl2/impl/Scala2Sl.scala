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

import scala.language.experimental.macros

import scala.reflect.macros.Context
import scala.reflect.api.{Universe}

import de.tuberlin.uebb.sl2.modules.{Syntax}

class Scala2Sl(val universe : Universe, rename : Map[String, String], val syntax : Syntax) {

  import universe._
  import syntax._

  case class SerializationException(msg : String) extends Exception(msg)

  var datadefs : List[DataDef] = Nil
  var scala2Sl : Map[String, ConVar] = Map("List" -> "List")

  val stringType = typeOf[String].typeSymbol.asClass.typeSignature
  val charType = typeOf[Char].typeSymbol.asClass.typeSignature
  val intType = typeOf[Int].typeSymbol.asClass.typeSignature
  val boolType = typeOf[Boolean].typeSymbol.asClass.typeSignature

  val builtins = Set(stringType, charType, intType, boolType)

  def currentProgram : Program = {
    Program(Map(), Map(), datadefs)
  }
  
  def fields(unapply : MethodSymbol, xargs : List[Type] = Nil) : List[Type] = {
    /* the presumed list of field types */
    unapply.returnType match {
      /* Option */
      case t@TypeRef(_, sym, arg::Nil) => {
        val inner = arg match {
          /* tuple */
          case t@TypeRef(_, _, args) if(t <:< typeOf[Product]) => args map {a => appliedType(a, xargs)}
          /* single type */
          case t@TypeRef(_, _, Nil) => appliedType(t, xargs)::Nil
        }
        inner
      }
    }    
  }

  def constructors(klazz : ClassSymbol) : Set[String] = {
    for (sub <- klazz.knownDirectSubclasses) yield rename.get(sub.name.toString).getOrElse(sub.name.toString)
  }

  def typevars(klazz : ClassSymbol) : List[String] = {
    for (p <- klazz.typeParams ; name = p.name.toString) yield rename.get(name).getOrElse(name) 
  }

  def scala2SlDataDef(klazz : ClassSymbol) : ConVar = {
    val name = klazz.name.toString
    val slname = rename.get(name).getOrElse(name)

    if (!scala2Sl.contains(name)) {
      scala2Sl = scala2Sl + (name -> slname)
      val nonAlgebraics = klazz.knownDirectSubclasses filter ({subClass => !subClass.asClass.isCaseClass })

      if (builtins.contains(klazz.typeSignature)) {        
      } else if(klazz.isCaseClass) {
        val unapply = klazz.companionSymbol.typeSignature.declaration(newTermName("unapply")).asMethod
        val members = fields(unapply)
      
        datadefs = DataDef(slname, Nil, ConstructorDef(slname, members map(scala2SlType(Map())))::Nil)::datadefs
      } else {
        if (!klazz.isSealed) {
          throw new SerializationException("Cannot serialize %s . It is not a sealed class.".format(klazz))
        }

        if (!nonAlgebraics.isEmpty) {
          throw new SerializationException("Cannot serialize %s . The following subclasses are no case classes: %s".format(klazz, nonAlgebraics.mkString(", ")))
        }

        val vars = klazz.typeParams.zipWithIndex.map(x => "t" + x._2)

        val constructors = klazz.knownDirectSubclasses map (sub => constructor(sub, klazz, vars map (TyVar(_))))
        datadefs = DataDef(slname, vars, constructors.toList)::datadefs      
      }
    }

    scala2Sl(name)
  }

  def scala2SlType(varMap : Map[Name, TyVar])(tipe : Type) : ASTType = {
    val sym = tipe.typeSymbol
    if (varMap.contains(sym.name))
      varMap(sym.name) 
    else {
      val tv = scala2SlDataDef(sym.asClass)
      val subs = tipe.asInstanceOf[TypeRef].args.map(scala2SlType(varMap))
      TyExpr(tv, subs)
    }
  }

  def constructor(sub : Symbol, parent : ClassSymbol, vars : List[TyVar]) : ConstructorDef = {
    val name = sub.name.toString
    val slname = rename.get(name).getOrElse(name)
    if (sub.isModuleClass) {
      ConstructorDef(slname, Nil)
    } else {
      val unapply = sub.companionSymbol.typeSignature.declaration(newTermName("unapply")).asMethod
      val members = fields(unapply)
      
      val subT = sub.typeSignature
      val extendsT = subT.baseType(parent).asInstanceOf[TypeRef]
      
      val varMap : Map[Name, TyVar] = Map() ++ (extendsT.args.map(_.typeSymbol.name).zip(vars))
      
      ConstructorDef(slname, members map(scala2SlType(varMap)))
    }
  }
  
}
