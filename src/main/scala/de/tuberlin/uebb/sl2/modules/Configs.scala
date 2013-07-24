package de.tuberlin.uebb.sl2.modules

import java.io.File

trait Configs {
  case class Config(
    val sourcepath: File,
    val sources: List[String],
    val classpath: File,
    val mainUnit: File,
    val destination: File)
}
