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
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import scala.io.Source
import scalax.io.JavaConverters._
import scalax.io._

/**
 * A driver that is able to compile more than one source file
 * at a time, using topological sorting
 */
trait MultiDriver extends Driver {
  self: Parser
    with AbstractFile
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
  	with ModuleLinearization =>
	
  override def run(config: Config): Either[Error, String] = {
    // load all (indirectly) required modules
    val modules = for(src <- config.sources) yield createModuleFromSourceFile(src, config)
    val errors = modules.filter(_.isLeft)
    if(errors.size > 0)
    	return Left(ErrorList(errors.map(_.left.get)))
    val dependencies = loadDependencies(modules, config, Map())    
    if(dependencies.isLeft) {
    	Left(dependencies.left.get)
      } else {
    	// sort topologically
	    val sortedModules = topoSort(dependencies.right.get)
	    ensureDirExists(config.destination)
	    // compile in topological order
	    val result =(for(modules <- sortedModules.right;
	    		     results <- errorMap(modules.toSeq.toList,
	    		    		 		handleSource(_ :Module, config)).right) yield "")
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
  
  def findModules(importedBy: Module, names: List[String], config:Config):Either[Error,List[Module]] = {
    errorMap(names, findModule(importedBy, _: String, config))
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
	    val module = moduleFromName(name, config)
	    if(!module.signature.canRead) {
	    	// no signature file exists
	    	if(module.source.canRead) {
	    	    Right(module.copy(compile = true))
	    	} else {
	    		Left(FilesNotFoundError("Module "+name+" imported by "+importedBy.name+" not found: ",
	    				module.source.path, module.signature.path))
	    	}
	    } else if(module.source.canRead() &&
	              (module.source.lastModified() > module.signature.lastModified())) {
	    	// a signature file exists, as well as a source file
	        Right(module.copy(compile = true))
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
    val module = moduleFromName(name, config)
	if(module.source.canRead) {
	    Right(module.copy(compile = true))
	} else {
		Left(FileNotFoundError(module.source.path))
	}
  }
  
  /**
   * Resolves the direct and transient dependencies of the given modules
   */
  def loadDependencies(modules: List[Either[Error,Module]], config: Config,
      dependencies: Map[Module,Set[Module]]):
      Either[Error,Map[Module,Set[Module]]] = modules match {
    case Left(e) :: rt => Left(e)
    case Right(m) :: rt => {
      val newDeps = loadModuleDependencies(m, config, dependencies)
      newDeps.right.flatMap(loadDependencies(rt, config, _))
    }
    case Nil => Right(dependencies)
  }

  /**
   * Resolves the direct and transient dependencies of the given module,
   * if it is not yet a key in the given dependencies map and if it is to
   * be compiled.
   */
  def loadModuleDependencies(module: Module, config: Config,
      dependencies: Map[Module,Set[Module]]):
      Either[Error, Map[Module,Set[Module]]] = {
    if((dependencies contains module) || !module.compile) {
      Right(dependencies)
    } else {
      val deps = getDirectDependencies(module, config)
      if(deps.isLeft) {
        Left(deps.left.get)
      } else {
        val depsR = deps.right.get.filter(_.compile) // ignore dependencies that need no compilation
        (Right(dependencies + (module -> depsR)).
            asInstanceOf[Either[Error, Map[Module,Set[Module]]]] /: depsR) {
          (currDeps, mod) => currDeps.right.flatMap(loadModuleDependencies(mod, config, _))
        }
      }
    }
  }
  
  /**
   * finds out the depencies of a given compilation unit
   */
  def getDirectDependencies(module: Module, config: Config): Either[Error, Set[Module]] = {
    // load input file
    val code = module.source.contents

    // parse the syntax
    fileName = module.source.path
    val ast = parseAst(code)
    
    // resolve dependencies
    for (
      mod <- ast.right;
      imports <- resolveDependencies(mod, config).right;
      x <- findModules(module, imports.toList, config).right
    ) yield x.toSet
  }
  
  def handleSource(module: Module, inputConfig: Config) = {
    // load input file
    val name = module.name
    println(inputConfig.sources+", "+name)
    //TODO: move main marking to Module?
    val isMain = inputConfig.sources.contains(name+".sl")
    val destination = inputConfig.destination
    val config = inputConfig.copy(mainName = module.source.filename, mainParent = module.source.parent, destination = destination)
    val code = module.source.contents
    
    for (
      (moq, compiled) <- compile(name, code, config).right;
      // output to fs
      res <- outputToFiles(moq, name, compiled, config).right;
      // create main.js while compiling main unit only if a main function is declared
      _ <- if (isMain && moq.functionDefs.contains("main"))
          generateMainJsFile(name, config).right
        else Right("No Main needed").right
    ) yield res

  }
  
  def compile(name: String, code: String, config: Config): Either[Error, Pair[Program, String]] = {
    val ast = parseAst(code)

    val checkResults = for (
      mo <- ast.right;
      // check and load dependencies
      imports <- inferDependencies(mo, config).right;
      // type check the program
      _ <- checkProgram(mo, normalizeModules(imports)).right)
      yield imports
      
    for (
      mo <- ast.right;
      // check and load dependencies
      imports <- checkResults.right;
      // qualify references to unqualified module
      moq <- Right(qualifyUnqualifiedModules(mo.asInstanceOf[Program], imports)).right;
      // synthesize
      compiled <- compileModuleBodyToString(moq, name, imports).right
    ) yield (moq, compiled)
  }
  
  def ensureDirExists(dir: File) = {
    // Create directory, if necessary
    if(!dir.exists()) {
      if(dir.mkdirs()) {
        println("Created directory "+dir)
      } else {
        println("Could not create directory"+dir)
      }
    } else if(!dir.isDirectory()) {
    	println(dir+" is not a directory")
    }
  }
  
  def compileSL(src: String, config: Config) = {
    for (
      (moq, compiled) <- compile("inline", src, config).right
    ) yield compiled 
  }
  
  def generateMainJsFile(name: String, config: Config) : Either[Error, String] = {
    val mainJs = new File(config.destination, "main.js")
    val libURL = getLibURL(config)
    if(libURL.isLeft)
      return Left(libURL.left.get)
	val stdURL = JsObject(List((JsName("std"), JsStr(libURL.right.get.toString))))
	val stdPath = JsObject(List((JsName("std"), JsStr(Paths.get(libURL.right.get.toURI).toString.replace("\\", "/")))))
    val mainWriter = new PrintWriter(mainJs)
    
    mainWriter.println("/***********************************/")
    mainWriter.println("// generated from: "+name)
    mainWriter.println("/***********************************/") 
    
    val mainTemplate = Source.fromURL(getClass.getResource("/js/main_template.js")).getLines.mkString("\n")
    mainWriter.write(mainTemplate.replace("%%MODULE_PATHS_LIST%%", "\""+name+".sl\"")
      .replace("%%STD_PATH%%", JsPrettyPrinter.pretty(stdPath))
      .replace("%%STD_URL%%", JsPrettyPrinter.pretty(stdURL))
      .replace("%%MODULE_NAMES_LIST%%", "$$$"+name.replace("/", "$").replace("\\", "$"))
      .replace("%%MAIN%%", JsPrettyPrinter.pretty(JsFunctionCall("$$$"+name.replace("/", "$").replace("\\", "$")+".$main"))))
    mainWriter.close()
    
    // copy index.html, require.js to config.destination
    copyResource("/js/index.html", new File(config.destination.getPath, "index.html").toURI)
    copyResource("/js/require.js", new File(config.destination.getPath, "require.js").toURI)
    
    return Right("generated main")
  }
  
  def outputToFiles(program: Program, name: String, compiled: String, config: Config): Either[Error, String] = {    
    val modulesDir = config.destination
    
    // compile js unit file
    val tarJs = new File(modulesDir, name + ".sl.js")
    ensureDirExists(tarJs.getParentFile())
    println("writing compiled "+name+" to "+tarJs)
    val moduleWriter = new PrintWriter(tarJs)
    moduleWriter.write(compiled)
    moduleWriter.close
    
    // write compiled external signature file
    val signatureFile = new File(modulesDir, name + ".sl.signature")
    println("writing signature of "+name+" to "+signatureFile)
    val writerSig = new PrintWriter(signatureFile)
    writerSig.write(serialize(program))
    writerSig.close()
    
    return Right("compilation successful")
  }
  
  def compileModuleBodyToString(program: Program, name: String, imports: List[ResolvedImport]): Either[Error, String] = {
    val moduleWriter = new StringWriter()
    val compiled = astToJs(program)
    val moduleTemplate = Source.fromURL(getClass().getResource("/js/module_template.js")).getLines.mkString("\n")
    insertExternallyImported(name, new PrintWriter(moduleWriter), imports) 
    val requires = imports.filter(_.isInstanceOf[ResolvedModuleImport]).map(
        x => JsDef(x.asInstanceOf[ResolvedModuleImport].name,
            JsFunctionCall(JsName("require"),JsStr(x.asInstanceOf[ResolvedModuleImport].path+".sl"))
        	))
    moduleWriter.write(moduleTemplate.replace("%%MODULE_BODY%%", JsPrettyPrinter.pretty(requires)+"\n\n"
        +JsPrettyPrinter.pretty(compiled)))
    moduleWriter.close()
    Right(moduleWriter.toString())
  }
  
  def insertExternallyImported(name: String, moduleWriter: PrintWriter, imports: List[ResolvedImport]) {
    for(i <- imports.filter(_.isInstanceOf[ResolvedExternImport])) {
      val imp = i.asInstanceOf[ResolvedExternImport]
      val includedCode = imp.file.contents
      moduleWriter.println("/***********************************/")
      moduleWriter.println("// included from: "+imp.file.path)
      moduleWriter.println("/***********************************/")
      moduleWriter.println(includedCode)
      moduleWriter.println("/***********************************/")
    }
    moduleWriter.println("/***********************************/")
    moduleWriter.println("// generated from: "+name)
    moduleWriter.println("/***********************************/") 
  }
  
  def getLibURL(config: Config):Either[Error,URL] = {
	  val libURL = getClass().getResource("/lib/")
      if(libURL == null) {
    	  Left(GenericError("Cannot compile: Standard library "+quote("/lib/")+" not found."))
      } else {
	      val libFile = createFile(libURL)
	      if(libFile.isInstanceOf[BottledFile]) {
	        val jarName = libFile.asInstanceOf[BottledFile].jarFile
	        val jar = new java.util.jar.JarFile(jarName)
	        val libEntry = jar.getJarEntry("/lib/")
	        val entries = jar.entries
	        while(entries.hasMoreElements()) {
	          val entry = entries.nextElement()
	          if(entry.getName.matches("^lib/.*\\.(signature|js)$")) {
	        	  copyResource(jar.getInputStream(entry),
	        	      jarName+"!"+entry.getName(),
	        	      new URI(config.destination.toURI.toString+entry.getName))
	          }
	        }
	        Right(new File(config.destination, "/lib/").toURI.toURL)
	      } else {
	        Right(new File(libFile.path).toURI.toURL)
	      }
      }
  }
  
  def copyResource(resourceName: String, to: URI):Unit = {
    copyResource(getClass().getResourceAsStream(resourceName),
        resourceName, to)
  }
  
  def copyResource(in: InputStream, inLabel: String, out: URI):Unit = {
	  val pathOption = scalax.file.Path(out)
	  if(pathOption.isDefined && !pathOption.get.exists) {
	    println("Copying "+inLabel+" to "+pathOption.get.path)
	    pathOption.get.createFile(true, true)
	    pathOption.get.doCopyFrom(Resource.fromInputStream(in))
	  }
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

