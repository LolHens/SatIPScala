package org.lolhens.satip.util

import scala.language.implicitConversions

class RichString2(val self: String) extends AnyVal {
  def padRight(len: Int, elem: String): String = self.padTo(len, elem).mkString

  def padLeft(len: Int, elem: String): String = self.reverse.padTo(len, elem).reverse.mkString
}

object RichString2 {
  implicit def fromString(string: String): RichString2 = new RichString2(string)
}