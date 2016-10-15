package org.connect.util.enrich

import cats.data.Xor

import scala.reflect.ClassTag

/**
  * Created by u016595 on 23.09.2016.
  */
class RichMap[K, V](val self: Map[K, V]) extends AnyVal {
  def getAs[T](key: K)(implicit classTag: ClassTag[T]): Option[T] =
    self.get(key).collect {
      case value: T => value
    }
}

object RichMap {
  implicit def fromMap[K, V](map: Map[K, V]): RichMap[K, V] = new RichMap(map)
}