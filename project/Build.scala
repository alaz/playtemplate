import sbt._
import sbt.Keys._

object TestBuild extends Build {
  val tImports = play.Project.templatesImport
  val tTypes = play.Project.templatesTypes

  lazy val root = Project(id = "playtemplate", base = file("."), settings = Project.defaultSettings ++ Seq(
    tImports  := Seq("app.templates._"),
    tTypes := {
      case _ => ("Content", "ContentFormat")
    },

    sourceGenerators in Compile <+= (state, sourceDirectory in Compile, sourceManaged in Compile, tTypes, tImports) map play.Project.ScalaTemplates

  ))
}