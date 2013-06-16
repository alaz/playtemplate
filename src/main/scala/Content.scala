package app.templates

import play.templates._

class Content(val buffer: StringBuilder) extends Appendable[Content] {
  def +=(other: Content) = {
    buffer.append(other.buffer)
    this
  }

  def body = buffer.toString
}

object Content {
  def apply(text: String) = new Content(new StringBuilder(text))
}

object ContentFormat extends Format[Content] {
  def raw(text: String) = Content(text)

  /**
   * Creates a safe (escaped) HTML fragment.
   *
   * @see framework/src/play/src/main/scala/play/api/templates/Templates.scala
   */
  def escape(text: String): Content = Content(text)
}