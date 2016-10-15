package org.connect.util

/**
  * Created by U016595 on 16.08.2016.
  */
class Enum[E](val name: String, val value: E)

abstract class EnumObj[E](val name: String) {
  val values: List[Enum[E]]

  def apply(value: E): Option[Enum[E]] = values.find(_.value == value)

  def unapply(enum: Enum[E]): E = enum.value
}
