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
import scala.text.Document
import scala.text.DocText
import de.tuberlin.uebb.sl2.modules._
import java.io.File
import java.io.PrintWriter
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import scala.io.Source

trait SimpleDriver extends Driver {
  self: Parser with CodeGenerator with Syntax with ProgramChecker with JsSyntax
  	with Errors with SignatureSerializer with DebugOutput with Configs
  	with ModuleResolver with ModuleNormalizer =>

  override def run(inpCfg: Config): Either[Error, String] = {
    val input = inpCfg.sources
    //TODO: implement for multiple files! at the moment only the first 
    // will be handled
    
    // load input file
    val file = new File(input.head)
    val name = file.getName()
    // if no destination has been specified, the output goes to the folder of the input file.
    val destination = if (inpCfg.destination == null) file.getParentFile() else inpCfg.destination
    val config = inpCfg.copy(mainUnit = file, destination = destination)
    val source = scala.io.Source.fromFile(file)
    val code = source.mkString
    source.close()

    // parse the syntax
    fileName = name
    val ast = parseAst(code)
    debugPrint(ast.toString());

    for (
      mo <- ast.right;
      // check and load dependencies
      imports <- inferDependencies(mo, config).right;
      // type check the program
      _ <- checkProgram(mo, normalizeModules(imports)).right;
      // qualify references to unqualified module and synthesize
      res <- compile(qualifyUnqualifiedModules(mo.asInstanceOf[Program], imports), name, imports, config).right
    ) yield res
  }
  
  def compile(program: Program, name: String, imports: List[ResolvedImport], config: Config): Either[Error, String] = {    
    val compiled = astToJs(program)
    
    // Create modules directory, if necessary
    val modulesDir = config.destination; // new File(config.destination, "modules")
    if(!modulesDir.exists()) {
      if(modulesDir.mkdirs()) {
        println("Created directory "+modulesDir)
      } else {
        println("Could not create directory"+modulesDir)
      }
    } else if(!modulesDir.isDirectory()) {
      println(modulesDir+" is not a directory")
    }
    
    // copy .js and .signature of imported modules from classpath to modules/ directory
    for(i <- imports.filter(_.isInstanceOf[ResolvedNamedImport])) {
      val imp = i.asInstanceOf[ResolvedNamedImport]
      copy(Paths.get(imp.file.toURI),   Paths.get(modulesDir.getAbsolutePath(), imp.path+".sl.signature"))
      copy(Paths.get(imp.jsFile.toURI), Paths.get(modulesDir.getAbsolutePath(), imp.path+".sl.js"))
    }
    
    val tarJs = new File(modulesDir, name + ".js")
    println("compiling "+name+" to "+tarJs)
    // TODO: maybe CombinatorParser does not yet parse qualified imports correctly, like ParboiledParser did before?
    val moduleTemplate = Source.fromURL(getClass().getResource("/js/module_template.js")).getLines.mkString("\n")
    val moduleWriter = new PrintWriter(new File(modulesDir, name + ".js"))
    for(i <- imports.filter(_.isInstanceOf[ResolvedExternImport])) {
      val imp = i.asInstanceOf[ResolvedExternImport]
      val includedCode = Source.fromFile(imp.file).getLines.mkString("\n")
      moduleWriter.println("/***********************************/")
      moduleWriter.println("// included from: "+imp.file.getCanonicalPath())
      moduleWriter.println("/***********************************/")
      moduleWriter.println(includedCode)
      moduleWriter.println("/***********************************/")
    }
    moduleWriter.println("/***********************************/")
    moduleWriter.println("// generated from: "+name)
    moduleWriter.println("/***********************************/") 

    val requires = imports.filter(_.isInstanceOf[ResolvedNamedImport]).map(
        x => JsDef(x.asInstanceOf[ResolvedNamedImport].name,
            JsFunctionCall(JsName("require"),JsStr(x.asInstanceOf[ResolvedNamedImport].path+".sl"))
        	))
    moduleWriter.write(moduleTemplate.replace("%%MODULE_BODY%%", JsPrettyPrinter.pretty(requires)+"\n\n"
        +JsPrettyPrinter.pretty(dataDefsToJs(program.dataDefs)
            & functionDefsExternToJs(program.functionDefsExtern)
            & functionDefsToJs(program.functionDefs))));
    moduleWriter.close();
    
    val signatureFile = new File(modulesDir, name + ".signature")
    println("writing signature of "+name+" to "+signatureFile)
    val writerSig = new PrintWriter(signatureFile)
    writerSig.write(serialize(program))
    writerSig.close()
    
    // create main.js only if a main function is declared
    if(program.isInstanceOf[Program] && program.asInstanceOf[Program].functionDefs.contains("main")) {
	    val mainWriter = new PrintWriter(new File(config.destination, "main.js"))
	    for(i <- imports.filter(_.isInstanceOf[ResolvedExternImport])) {
	      val imp = i.asInstanceOf[ResolvedExternImport]
	      val includedCode = Source.fromFile(imp.file).getLines.mkString("\n")
	      mainWriter.println("/***********************************/")
	      mainWriter.println("// included from: "+imp.file.getCanonicalPath())
	      mainWriter.println("/***********************************/")
	      mainWriter.println(includedCode)
	      mainWriter.println("/***********************************/")
	    }
	    mainWriter.println("/***********************************/")
	    mainWriter.println("// generated from: "+name)
	    mainWriter.println("/***********************************/") 
	    val mainTemplate = Source.fromURL(getClass.getResource("/js/main_template.js")).getLines.mkString("\n")
	    mainWriter.write(mainTemplate.replace("%%MODULE_PATHS_LIST%%", "\""+name+"\"")
	    	.replace("%%MODULE_NAMES_LIST%%", "$$$"+name.substring(0, name.length()-3))
	    		.replace("%%MAIN%%", JsPrettyPrinter.pretty(JsFunctionCall("$$$"+name.substring(0, name.length()-3)+".$main"))))
	    mainWriter.close()
	    
	    // copy index.html, require.js to config.destination
	    copy(Paths.get(getClass().getResource("/js/index.html").toURI()),
	        Paths.get(config.destination.getAbsolutePath(), "index.html"))
	    copy(Paths.get(getClass().getResource("/js/require.js").toURI()),
	        Paths.get(config.destination.getAbsolutePath(), "require.js"))
    }
    
    return Right("compilation successful")
  }
  
  def copy(from: Path, to: Path) = {
    if(!to.getParent.toFile.exists) {
    	if(!to.getParent.toFile.mkdirs) {
    	  // TODO: return an error
    	  println("Could not create directory: "+to.getParent)
    	}
    } else if (to.getParent.toFile.exists && !to.getParent.toFile.isDirectory) {
    	// TODO: return an error
      println("Not a directory: "+to.getParent)
    }
    val target = Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING)
    println("copied "+from+" to "+to);
    target
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
      val intersect = a.keySet & b.keySet
      if (intersect.isEmpty)
        Right(a ++ b)
      else
        Left(DuplicateError("Duplicated definition: " + intersect.mkString(", "), "", Nil))
    }
}

