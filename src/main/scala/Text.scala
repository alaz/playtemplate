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