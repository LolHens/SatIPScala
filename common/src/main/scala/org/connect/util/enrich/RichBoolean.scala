package org.connect.util.enrich

/**
  * Created by Pierre on 07.03.2016.
  */
class RichBoolean(val self: Boolean) extends AnyVal {
  def Then[A](value: => A): Option[A] = if (self) Some(value) else None

  def ThenIf[A](value: => Option[A]): Option[A] = if (self) value else None
}

object RichBoolean {
  implicit def fromBoolean(value: Boolean): RichBoolean = new RichBoolean(value)
}
