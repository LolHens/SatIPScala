package org.lolhens.satip.util

object StringUtils {

  implicit class StringOps(val self: String) extends AnyVal {
    def padRight(len: Int, elem: String = " "): String =
      if (self.length < len) {
        val padding = len - self.length
        self + (elem * ((padding / elem.length) + 1)).take(padding)
      } else
        self

    def padLeft(len: Int, elem: String = " "): String =
      if (self.length < len) {
        val padding = len - self.length
        (elem * ((padding / elem.length) + 1)).take(padding) + self
      } else
        self
  }

}
