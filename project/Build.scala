import sbt._
import sbt.Keys._

object PlayTemplateBuild extends Build {
  import Templates._

  lazy val buildSettings = Seq(
    scalaVersion := "2.10.2"
  )

  lazy val projectSettings = buildSettings ++ templateSettings ++ Seq(
    mainClass in Compile := Some("Main"),
    resolvers += templatesResolver,
    libraryDependencies += templatesLibrary
  )

  lazy val root = Project(
    id = "playtemplate",
    base = file("."),
    settings = Project.defaultSettings ++ projectSettings)
}

object Templates {
  // What are the packages where content types & formats are defined
  val templatePackages = SettingKey[Seq[String]]("template-packages")

  // The mapping from file extension to the content type T and its format Format[T]
  val templateFormats = SettingKey[PartialFunction[String, (String, String)]]("template-formats")

  lazy val templateDefaults = Seq[Setting[_]](
    templatePackages := Seq("app.templates._"),
    templateFormats := {
      case _ => ("Text", "TextFormat")
    }
  )

  lazy val templateSettings = templateDefaults ++ Seq(
    sourceGenerators in Compile <+= (state, sourceDirectory in Compile, sourceManaged in Compile, templateFormats, templatePackages) map play.Project.ScalaTemplates
  )

  val templatesResolver = "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"
  val templatesLibrary = "play" %% "templates" % play.core.PlayVersion.current
}