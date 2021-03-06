import sbt._
import sbt.Keys._

object PlayTemplateBuild extends Build {
  import Templates._

  lazy val buildSettings = Seq(
    scalaVersion := "2.10.3"
  )

  lazy val projectSettings = buildSettings ++ templateSettings ++ Seq(
    mainClass in Compile := Some("Main")
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
  val templateFormats = SettingKey[Map[String, String]]("template-formats")

  lazy val templateDefaults = Seq[Setting[_]](
    templatePackages := Seq("app.templates._"),
    templateFormats := Map("html" -> "TextFormat")
  )

  val templatesResolver = "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"
  val templatesLibrary = "com.typesafe.play" %% "templates" % play.core.PlayVersion.current

  lazy val templateSettings = templateDefaults ++ Seq(
    resolvers += templatesResolver,
    libraryDependencies += templatesLibrary,
    sourceGenerators in Compile <+= (state, unmanagedSourceDirectories in Compile, sourceManaged in Compile, templateFormats, templatePackages) map play.Project.ScalaTemplates
  )
}
