import AssemblyKeys._

assemblySettings

name := "sl"

organization := "de.tuberlin.uebb"

version := "0.1.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation", "-feature", "-Xmax-classfile-name", "140", "-unchecked")

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "1.9.1",
	"org.parboiled" %% "parboiled-scala" % "1.1.4",
	"org.mozilla" % "rhino" % "1.7R4",
	"com.googlecode.kiama" %% "kiama" % "1.4.0",
	"net.sf.jgrapht" % "jgrapht" % "0.8.3",
	"com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
)

// Skip tests during assembly
test in assembly := {} 