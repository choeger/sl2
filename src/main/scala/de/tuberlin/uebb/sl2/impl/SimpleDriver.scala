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
import java.io.File
import java.io.PrintWriter
import scala.io.Source

trait SimpleDriver extends Driver {
  self: Parser with CodeGenerator with Syntax with ProgramChecker with JsSyntax
  	with Errors with SignatureSerializer with DebugOutput with Configs
  	with ModuleResolver with ModuleNormalizer =>

  override def run(inpCfg: Config): Either[Error, String] = {
    val input = inpCfg.sources
    //TODO: implement for multiple files! at the moment only the first 
	// will be handled.
	
	// load input file
	val file = new File(input.head)
	val name = file.getName()
	val config = inpCfg.copy(classpath = file.getParentFile(), mainUnit = file)
	val source = scala.io.Source.fromFile(file)
	val code = source.mkString
	source.close()
	
	// parse the syntax 
	val ast = parseAst(code)
	debugPrint(ast.toString());
	
	for (
      mo <- ast.right;
      // check and load dependencies
      imports <- inferDependencies(mo, config).right;
      // type check the program
      _ <- checkProgram(mo, normalizeModules(imports)).right;
      
      // and synthesize
      res <- compile(mo, name, imports, config).right
    ) yield res
  }
  
  def compile(program: AST, name: String, imports: List[ResolvedImport], config: Config): Either[Error, String] = {
    //TODO: is there a more functional way to do file io in scala?
    val tarJs = new File(config.destination, name + ".js")
    val tarSig = new File(config.destination, name + ".signature")
    
    val compiled = astToJs(program)
    println("compiling "+name+" to "+tarJs)
    val writerProg = new PrintWriter(tarJs)
    for(i <- imports.filter(_.isInstanceOf[ResolvedExternImport])) {
      val imp = i.asInstanceOf[ResolvedExternImport]
      val includedCode = Source.fromFile(imp.file).getLines.mkString("\n")
      writerProg.println("/***********************************/")
      writerProg.println("// included from: "+imp.file.getCanonicalPath())
      writerProg.println("/***********************************/")
      writerProg.println(includedCode)
      writerProg.println("/***********************************/")
    }
    writerProg.println("/***********************************/")
    writerProg.println("// generated from: "+name)
    writerProg.println("/***********************************/") 
    writerProg.write(JsPrettyPrinter.pretty(compiled))
    writerProg.close()
    
    println("writing signature of "+name+" to "+tarSig)
    val writerSig = new PrintWriter(tarSig)
    writerSig.write(serialize(program))
    writerSig.close()
    
    return Right("compilation successful")
  }

  def mergeAst(a: Program, b: Program): Either[Error, Program] =
    {
      for (
        sigs <- mergeMap(a.signatures, b.signatures).right;
        funs <- mergeMap(a.functionDefs, b.functionDefs).right;
        funsEx <- mergeMap(a.functionDefsExtern, b.functionDefsExtern).right
      ) yield {
        val defs = a.dataDefs ++ b.dataDefs
        Program(List(), sigs, funs, funsEx, defs)
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

