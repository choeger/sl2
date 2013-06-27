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
  * The module defining the structured error representation.
  */
trait Errors {

  self: Syntax =>
  
  abstract class Error {
    /**
      * Produce an error message.
      */
    def message: String = "error"

    /**
      * Combine two errors into an error list.
      */
    def <+>(that: Error): ErrorList = ErrorList(List(this, that))
  }

  object LocatedError {
    def unapply(e : Error) : Option[(String, Location)] = e match {
      case UndefinedError(_, _, AttributeImpl(l)) => Some((message(e), l))
      case _ => None
    }
  }

  def message(e : Error) : String = e match {
    case UndefinedError(what, name, _) => "Undefined %s '%s'".format(what, name)
    case _ => e.toString //TODO
  }

  /* Generic error with a static error message */
  case class GenericError(what: String) extends Error

  /* If a phase depends on another phase that produced an error, forward that error. */
  case class CouldNotRun(what : String, why : Error) extends Error

  /* Groups of errors */
  case class ErrorList(errors : List[Error]) extends Error
  
  /* Useful during implementation */
  case object NotYetImplemented extends Error

  /* Parser: parse error */
  case class ParseError(msg : String, startIndex : Int, endIndex : Int) extends Error

  /* Type checker: undefined element error */
  case class UndefinedError(what: String, name: String, where: Attribute) extends Error

  /* Type checker: type error */
  case class TypeError(what: String, where: Attribute, cause: Error) extends Error

  /* Context analysis: duplicate error */
  case class DuplicateError(what: String, name: String, where: List[Attribute]) extends Error

  /* Context analysis: generic error with location hint */
  case class AttributedError(what: String, where: Attribute) extends Error


  /**
    * Apply a function which might result in an Error to all elements of a list
    * and combine the results accordingly, i.e., all occurring errors are collected
    * and stored an an ErrorList.
    */
  def errorMap[S,T](ts: List[T], f: T => Either[Error, S]): Either[Error, List[S]] = {

    def combine[T](e1: Either[Error, T], e2: Either[Error, List[T]]) = e1 match {
      case Left(error1) => e2 match {
	case Left(ErrorList(errors)) => Left(ErrorList(error1 :: errors))
	case Left(error2)            => Left(error1 <+> error2)
	case _                       => Left(error1)
      }
      case Right(s) => e2 match {
	case Right(t)    => Right(s :: t)
	case Left(error) => Left(error)
      }
    }

    val z: Either[Error, List[S]] = Right(Nil)
    (ts.map(f) :\ z)(combine)
  }


  /**
    * Collect errors over two computations.
    */
  def collectErrors(e1: Either[Error, Unit], e2: Either[Error, Unit]) = e1 match {
    case Left(error1) => e2 match {
      case Left(error2) => Left(error1 <+> error2)
      case Right(_)     => Right()
    }

    case Right(_) => e2 match {
      case Left(error) => Left(error)
      case Right(_)    => Right()
    }
  }

  
  /**
    * Put a string in `quotes'. Useful for producing pretty error messages.
    */
  def quote(str: String): String = "`" + str + "'"

  /**
    * Quote a list of strings.
    */
  def quote(list: List[String]): String = list.map(quote).mkString(", ")

  /**
    * Quote a set of strings.
    */
  def quote(set: Set[String]): String = quote(set.toList)
}
