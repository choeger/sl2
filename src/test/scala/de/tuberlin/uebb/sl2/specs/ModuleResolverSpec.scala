package de.tuberlin.uebb.sl2.tests.specs

import org.scalatest.matchers._
import org.scalatest.FunSpec
import de.tuberlin.uebb.sl2.modules._

trait ModuleResolverSpec extends FunSpec with ShouldMatchers {
  this : ModuleResolver with Syntax with Errors =>
  
  def testedImplementationName(): String

  def notFail: Matcher[Either[Error, Unit]] = be(Right())
  
  def fail(err: Error) : Matcher[Either[Error, Unit]] = be(Left(err))

  describe(testedImplementationName() + ": import statement") {
    it("Should accept unique paths") {
      val imports = List(
        QualifiedImport("my/path", "MyModule"),
        QualifiedImport("other/path", "OtherModule")
      )
      
      checkImports(imports) should notFail
    }
        
    it("Should accept correct paths") {
      val imports = List(
        QualifiedImport("my/path", "Ordinary"),
        QualifiedImport("my/path/_underscore_", "Underscores"),
        QualifiedImport("my/path/num83r5", "Numbers"),
        QualifiedImport("my/path/-", "Dash"),
        QualifiedImport("my/.hidden/path", "Dot"),
        QualifiedImport("my/.../path", "Dots")
      )
      
      checkImports(imports) should notFail
    }

    it("Should accept unique module identifiers") {
      val imports = List(
        QualifiedImport("my/path", "MyModule"),
        QualifiedImport("other/path", "OtherModule")
      )
      
      checkImports(imports) should notFail
    }
    
    it("Should refuse incorrect paths") {
      val imports = List(
        QualifiedImport("", "Empty"),
        QualifiedImport(".", "Current"),
        QualifiedImport("..", "Parent"),
        QualifiedImport("/module", "Root"),
        QualifiedImport("dir/", "Directory")
      )
      
      checkImports(imports) should fail(InvalidPathError)
    }
    
    it("Should refuse duplicate paths") {
      val imports = List(
        QualifiedImport("identical/path", "MyModule"),
        QualifiedImport("identical/path", "OtherModule")
      )
      
      checkImports(imports) should fail(DuplicatePathError)
    }
    
    it("Should refuse duplicate module identifiers") {
      val imports = List(
        QualifiedImport("my/path", "Duplicate"),
        QualifiedImport("other/path", "Duplicate")
      )
      
      checkImports(imports) should fail(DuplicateModuleError)
    }
  }
  
  describe(testedImplementationName() + ": import resolution") {
    it("Should resolve a qualified import") {
      // TODO
    }
    
    it("Should resolve an unqualified import") {
      // TODO
    }
    
    it("Should resolve an external import") {
      // TODO
    }
    
    // TODO missing files
  }
  
}
