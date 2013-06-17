# Play2 templates in non-Play project

Play2 templates are cool because they got compiled into Scala code and this Scala code is then built along with the rest of the project. Thus the templates are

* statically checked with the application code
* pre-compiled

This is a demo project to show how [Play2 templates](http://www.playframework.com/documentation/2.1.0/ScalaTemplates) can be used in a standalone SBT project.

## Build time

### Play2 plugin

Play2 code that compiles templates is buried inside "sbt-plugin", so one needs to attach it to the SBT build, see [project/templates.sbt](blob/master/project/templates.sbt):

```scala
addSbtPlugin("play" % "sbt-plugin" % "2.1.1")
```

### Source generation parameters

`sbt-plugin` contains convenient source generator called `ScalaTemplates`, it depends on the couple parameters:

```scala
// What are the packages where content types & formats are defined
val templatePackages = SettingKey[Seq[String]]("template-packages")

// The mapping from file extension to the content type T and its format Format[T]
val templateFormats = SettingKey[PartialFunction[String, (String, String)]]("template-formats")
```

(more on the "content type" below in *Runtime* section)

Then the source generator looks like 

```scala
sourceGenerators in Compile <+= (state, sourceDirectory in Compile, sourceManaged in Compile, templateFormats, templatePackages) map play.Project.ScalaTemplates
```

(`play.Project.*` is provided by "sbt-plugin")

The full example is here [project/Build.scala](blob/master/project/Build.scala)

## Runtime

### Dependency

The compiled templates depend on the small library:

```scala
val templatesLibrary = "play" %% "templates" % play.core.PlayVersion.current
```

(here again `play.core.PlayVersion.current` is provided by "sbt-plugin")

### Content type

When "ScalaTemplates" generate Scala files from templates, they are parametrized with so called content type. When calling template (e.g. `views.html.hello("World")`), one gets the instance of such type. Remember that `Html` in Play2 applications? `Html`, `Txt` types are hidden in `play.api` library, but it's very easy to create own content type:

```scala
package app.templates

import play.templates._

class Text(val buffer: StringBuilder) extends Appendable[Text] {
  def +=(other: Text) = {
    buffer.append(other.buffer)
    this
  }

  def body = buffer.toString
}

object Text {
  def apply(text: String) = new Text(new StringBuilder(text))
}

object TextFormat extends Format[Text] {
  def raw(text: String) = Text(text)

  /**
   * Creates a safe (escaped) content for this format (e.g. escaped for HTML and plain for Txt)
   *
   * @see framework/src/play/src/main/scala/play/api/templates/Templates.scala
   */
  def escape(text: String): Text = Text(text)
}
```

See [src/main/scala/Text.scala](blob/master/src/main/scala/Text.scala)

`templatePackages` and `templateFormats` in SBT build tell "sbt-plugin" what imports and types to use in the generated Scala files.

### Using

Trivial:

```scala
println(views.html.hello("World").body)
```

See [src/main/scala/Main.scala](blob/master/src/main/scala/Main.scala)

## Problems

Are any?
