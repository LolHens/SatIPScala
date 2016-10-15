package org.connect.util.enrich

import zak.util.enrich.RichBoolean._
import zak.util.enrich.RichOption._

/**
  * Created by Pierre on 02.11.2015.
  */
class RichAny[A](val self: A) extends AnyVal {
  def If(condition: A => Boolean): Option[A] = {
    condition(self) Then {
      Some(self)
    } Else {
      None
    }
  }

  def If(condition: => Boolean): Option[A] =
    If((e) => condition)
}

object RichAny {
  implicit def fromAny[A](value: A): RichAny[A] = new RichAny[A](value)
}