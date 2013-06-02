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

import org.kiama.rewriting.Rewriter._
import de.tuberlin.uebb.sl2.impl.CombinatorParser

trait PreProcessing {

  this: Syntax with SyntaxTraversal with Errors =>
    
  val replaceCustomMap = Map ( "\\!" -> "\\$q"
                             , "\\§" -> "\\$w"
                             , "\\%" -> "\\$r"
                             , "\\&" -> "\\$t"
                             , "\\/" -> "\\$z"
                             , "\\=" -> "\\$u"
                             , "\\?" -> "\\$i"
                             , "\\+" -> "\\$o"
                             , "\\*" -> "\\$p"
                             , "\\#" -> "\\$a"
                             , "\\-" -> "\\$s"
                             , "\\:" -> "\\$f"
                             , "\\<" -> "\\$g"
                             , "\\>" -> "\\$h"
                             , "\\|" -> "\\$j"
                             , "\\." -> "__"
                             )    
  
  
  val replaceBuiltinMap = Map ( "+"  -> "_add"
                              , "+s" -> "_adds"
                              , "+r" -> "_addr"
                              , "*r" -> "_mulr"
                              , "/r" -> "_divr"
                              , "-r" -> "_subr"
                              , "-"  -> "_sub"
                              , "*"  -> "_mul"
                              , "/"  -> "_div"
                              , "==" -> "_eq"
                              , ">=" -> "_geq"
                              , "<=" -> "_leq"
                              , "<"  -> "_lesser"
                              , ">"  -> "_greater"
                              , "&=" -> "_bind"
                              , "&"  -> "_bindnr"
                              , "yield" -> "_yield"
                              , "stol" -> "_stol"
                              , "ltos" -> "_ltos"
			      )
  

  def renameIdentifier(a: AST): AST = {
    val f : PartialFunction[String, String] = {case s: String => escapeJsIde(s)}
    map(f, a)
  }
  
  def preprocessing(a: AST): AST = renameIdentifier(a)

  def escapeJsIde(x: String): String = {
    var a = x

    if (replaceBuiltinMap.contains(x)) a = replaceBuiltinMap(x)
    
    replaceCustomMap.foreach {
      case (k, v) => a = a.replaceAll(k, v)
    }
    
    if (a(0) == '$' || a(0) == '_') a
    else "$" + a
  }
  
  def $ (x: String): String = escapeJsIde(x)
}
