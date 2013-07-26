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
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import scala.io.Source

/**
 * A driver that is able to compile more than one source file
 * at a time, using topological sorting to
 */
trait MultiDriver extends Driver {
  self: Parser
  	with CodeGenerator
  	with Syntax
  	with ProgramChecker
  	with JsSyntax
  	with Errors
  	with SignatureSerializer
  	with DebugOutput
  	with Configs
  	with ModuleResolver
  	with ModuleNormalizer
  	with TopologicalSorting =>
	
  override def run(config: Config): Either[Error, String] = {
    println("classpath="+config.classpath)
    // load all (indirectly) required modules
    val modules = for(src <- config.sources) yield createModuleFromSourceFile(src, config)
    val dependencies = loadDependencies(modules, config, Map[Module,Set[Module]]())    
    println("modules="+modules)
    println("dependencies="+dependencies)
    if(dependencies.isLeft) {
    	Left(dependencies.left.get)
    } else {
    	// sort topologically
	    val sortedModules = topoSort(dependencies.right.get)
	    println("sortedModules="+sortedModules)
	    prepareCompilation(config)
	    // compile in topological order
	    val result =(for(modules <- sortedModules.right;
	    		     results <- errorMap(modules.toSeq.toList, handleModuleSource(config)).right) yield "")
	    if(result.isLeft) {
	      result
	    } else {
	      Right("compilation successful")
	    }
    }
  }
  
  def createModuleFromSourceFile(fileName: String, config: Config):Either[Error,Module] = {
    if(fileName endsWith ".sl") {
      findModuleFromSource(fileName.substring(0, fileName.length-3), config)
    } else {
      Left(GenericError("Cannot compile: SL source file "+quote(fileName)+" does not end with suffix .sl"))
    }
  }
  
  def findModules(importedBy: Module, names: Set[String], config:Config):Either[Error,Set[Module]] = {
    var modules:Either[Error,Set[Module]] = Right(Set[Module]())
    for (name <- names) {
	      if(modules.isRight) {
	    	  val module = findModule(importedBy, name, config)
	    	  if(module.isLeft) {
	    	    modules = Left(module.left.get)
	    	  } else {
	    	    modules = Right(modules.right.get + module.right.get)
	    	  }
	      }
      }
    modules
  }
  
  /**
   * Create an appropriate module object, based on available files:
   * 
   * <ol>
   * <li>If there is no signature file on the classpath for the imported
   *     file or the source file was given explicitly, create the module
   *     to be compiled from its source file. Return an error, if the
   *     source file does not exist.</li>
   * <li>If there is a signature file and a source file, and the source
   * 	 file is younger, create a module to be compiled from its source
   *     file.</li>
   * <li>Otherwise create a module that is not to be compiled.</li>    
   */
  def findModule(importedBy: Module, name: String, config: Config):Either[Error,Module] = {
    try {
	    val module = new Module(name, config)
	    if(!module.signatureFile.canRead()) {
	    	// no signature file exists
	    	if(module.sourceFile.canRead()) {
	    		module.compile = true
	    	    Right(module)
	    	} else {
	    		Left(FilesNotFoundError("Module "+name+" imported by "+importedBy.name+" not found: ",
	    				module.sourceFile, module.signatureFile))
	    	}
	    } else if(module.sourceFile.canRead() &&
	              module.sourceFile.lastModified() >= module.signatureFile.lastModified()) {
	    	// a signature file exists, as well as a source file
	        module.compile = true
	        Right(module)
	    } else {
	    	// a signature, but no source file exists: load from signature
	    	Right(module)
	    }
    } catch {
      case ioe: IOException => Left(GenericError(ioe.getMessage()))
    }
  }
  
  /**
   * Create a module to be compiled from its source file.
   */
  def findModuleFromSource(name: String, config: Config): Either[Error,Module] = {
    val module = new Module(name, config)
	if(module.sourceFile.canRead()) {
	    module.compile = true
	    Right(module)
	} else {
		Left(FileNotFoundError(module.sourceFile))
	}
  }
  
  /**
   * Resolves the direct and transient dependencies of the given modules
   * if they are not yet a key in the given dependencies map.
   */
  def loadDependencies(modules: List[Either[Error,Module]], config: Config,
      dependencies: Map[Module,Set[Module]]): Either[Error,Map[Module,Set[Module]]] = {
    var deps:Either[Error,Map[Module,Set[Module]]] = Right(dependencies)
    for (module <- modules; mod <- module.right) {
	      if(!deps.isLeft)
	        deps = loadDependencies(mod, config, deps.right.get)
      }
    deps
  }
  
  /**
   * Resolves the direct and transient dependencies of the given module,
   * if it is not yet a key in the given dependencies map and if it is to
   * be compiled.
   */
  def loadDependencies(module: Module, config: Config,
      dependencies: Map[Module,Set[Module]]): Either[Error,Map[Module,Set[Module]]] = {
    if((dependencies contains module) || !module.compile) {
      Right(dependencies)
    } else {
      val deps = getDependencies(module, config)
      if(deps.isLeft) {
        Left(deps.left.get)
      } else {
    	  val depsR = deps.right.get.filter(_.compile) // ignore dependencies that need no compilation
	      var newDeps = dependencies + (module -> depsR)
	      for (dep <- Right(depsR).right;
	    	   de <- dep) {
	        val result = loadDependencies(de, config, newDeps)
	        if(result.isLeft) return result else newDeps=result.right.get }
	      Right(newDeps)
      } 
    }
  }
  
  def getDependencies(module: Module, config: Config): Either[Error, Set[Module]] = {
    // load input file
    val source = scala.io.Source.fromFile(module.sourceFile)
    val code = source.mkString
    source.close()

    // parse the syntax
    fileName = module.sourceFile.getName
    val ast = parseAst(code)
    
    // resolve dependencies
    val r = (for (
      mod <- ast.right;
      imports <- resolveDependencies(mod, config).right;
      x <- findModules(module, imports, config).right
    ) yield x)
    println("dependencies of "+module.name+": "+r)
    r
  }
  
  def handleModuleSource(config: Config)(module: Module) = {
    handleSource(module.sourceFile, config)
  }
  
  def handleSource(file: File, inputConfig: Config) = {
    // load input file
    val name = file.getName()
    // if no destination has been specified, the output goes to the folder of the input file.
    // TODO: implement a test for this
    val destination = if (inputConfig.destination == null) file.getParentFile() else inputConfig.destination
    val config = inputConfig.copy(mainUnit = file, destination = destination)
    val source = scala.io.Source.fromFile(file)
    val code = source.mkString
    source.close()

    // parse the syntax
    fileName = name
    val ast = parseAst(code)
    //debugPrint(ast.toString());

    val checkResults = for (
      mo <- ast.right;
      // check and load dependencies
      imports <- inferDependencies(mo, config).right;
      // type check the program
      _ <- checkProgram(mo, normalizeModules(imports)).right) yield imports
    
    if(checkResults.isLeft) {
      checkResults
    } else {
    	// generate code, if checks were successful
	    for (
	      mo <- ast.right;
	      // check and load dependencies
	      imports <- checkResults.right;
	      // qualify references to unqualified module and synthesize
	      res <- compile(qualifyUnqualifiedModules(mo.asInstanceOf[Program], imports), name, imports, config).right
	    ) yield res
    }
  }
  
  def compileSL(src: String, config: Config) = {
    // parse the syntax
    fileName = ""
    val ast = parseAst(src)
    //debugPrint(ast.toString());

    val checkResults = for (
      mo <- ast.right;
      // check and load dependencies
      imports <- inferDependencies(mo, config).right;
      // type check the program
      _ <- checkProgram(mo, normalizeModules(imports)).right) yield imports
    
    if(checkResults.isLeft) {
      Left(checkResults.left.get)
    } else {
    	// generate code, if checks were successful
	    for (
	      mo <- ast.right;
	      // check and load dependencies
	      imports <- checkResults.right
	    ) yield (
	      // qualify references to unqualified module and synthesize
        compileToString(qualifyUnqualifiedModules(mo.asInstanceOf[Program], imports), imports)
      )
    }
  }
  
  def prepareCompilation(config: Config) = {
    // Create modules directory, if necessary
    val modulesDir = config.destination;//new File(config.destination, "modules")
    if(!modulesDir.exists()) {
      if(modulesDir.mkdirs()) {
        println("Created directory "+modulesDir)
      } else {
        println("Could not create directory"+modulesDir)
      }
    } else if(!modulesDir.isDirectory()) {
      println(modulesDir+" is not a directory")
    }
  }
  
  def compileToString(program: Program, imports: List[ResolvedImport]) = {
    // TODO: maybe CombinatorParser does not yet parse qualified imports correctly, like ParboiledParser did before?
    val moduleTemplate = Source.fromURL(getClass().getResource("/js/module_template.js")).getLines.mkString("\n")
    val stringWriter = new StringWriter()
    val moduleWriter = new PrintWriter(stringWriter)
    for(i <- imports.filter(_.isInstanceOf[ResolvedExternImport])) {
      val imp = i.asInstanceOf[ResolvedExternImport]
      val includedCode = Source.fromFile(imp.file).getLines.mkString("\n")
      moduleWriter.println("/***********************************/")
      moduleWriter.println("// included from: "+imp.file.getCanonicalPath())
      moduleWriter.println("/***********************************/")
      moduleWriter.println(includedCode)
      moduleWriter.println("/***********************************/")
    }

    val requires = imports.filter(_.isInstanceOf[ResolvedModuleImport]).map(
        x => JsDef(x.asInstanceOf[ResolvedModuleImport].name,
            JsFunctionCall(JsName("require"),JsStr(x.asInstanceOf[ResolvedModuleImport].path+".sl"))
        	))
    moduleWriter.write(moduleTemplate.replace("%%MODULE_BODY%%", JsPrettyPrinter.pretty(requires)+"\n\n"
        +JsPrettyPrinter.pretty(dataDefsToJs(program.dataDefs)
            & functionDefsExternToJs(program.functionDefsExtern)
            & functionDefsToJs(program.functionDefs)
            & functionSigsToJs(program.signatures))));
    stringWriter.toString
  }
  
  def compile(program: Program, name: String, imports: List[ResolvedImport], config: Config): Either[Error, String] = {    
    val compiled = astToJs(program)
    val modulesDir = config.destination
    
    // copy .js and .signature of imported modules from classpath to modules/ directory
    // TODO: should not copy prelude on every compiled module
    // TODO: should not copy modules that have just been compiled
/*
    for(i <- imports.filter(_.isInstanceOf[ResolvedModuleImport])) {
      val imp = i.asInstanceOf[ResolvedModuleImport]
      copy(Paths.get(imp.file.toURI),   Paths.get(modulesDir.getAbsolutePath(), imp.path+".sl.signature"))
      copy(Paths.get(imp.jsFile.toURI), Paths.get(modulesDir.getAbsolutePath(), imp.path+".sl.js"))
    }
 */    
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

    val requires = imports.filter(_.isInstanceOf[ResolvedModuleImport]).map(
        x => JsDef(x.asInstanceOf[ResolvedModuleImport].name,
            JsFunctionCall(JsName("require"),JsStr(x.asInstanceOf[ResolvedModuleImport].path+".sl"))
        	))
    moduleWriter.write(moduleTemplate.replace("%%MODULE_BODY%%", JsPrettyPrinter.pretty(requires)+"\n\n"
        +JsPrettyPrinter.pretty(dataDefsToJs(program.dataDefs)
            & functionDefsExternToJs(program.functionDefsExtern)
            & functionDefsToJs(program.functionDefs)
            & functionSigsToJs(program.signatures))));
    moduleWriter.close();
    
    val signatureFile = new File(modulesDir, name + ".signature")
    println("writing signature of "+name+" to "+signatureFile)
    val writerSig = new PrintWriter(signatureFile)
    writerSig.write(serialize(program))
    writerSig.close()
    
    // create main.js only if a main function is declared
    if(program.isInstanceOf[Program] && program.asInstanceOf[Program].functionDefs.contains("main")) {
      val mainJs = new File(config.destination, "main.js")
      val paths = JsObject(List((JsName("std"), JsStr(getClass().getResource("/lib/").toString))))
    	println("creating "+mainJs.getAbsolutePath)
	    val mainWriter = new PrintWriter(mainJs)
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
              .replace("%%PATHS%%", JsPrettyPrinter.pretty(paths))
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

