package org.lolhens.satip.rtsp2

import scala.collection.generic.CanBuildFrom
import scala.collection.{immutable, mutable}

case class Headers private(headers: List[Header])
  extends immutable.Iterable[Header]
    with collection.IterableLike[Header, Headers] {
  override def toList: List[Header] = headers

  override def isEmpty: Boolean = headers.isEmpty

  override protected def newBuilder: mutable.Builder[Header, Headers] = Headers.newBuilder

  override def drop(n: Int): Headers = if (n == 0) this else new Headers(headers.drop(n))

  override def head: Header = headers.head

  override def foreach[B](f: Header => B): Unit = headers.foreach(f)

  def iterator: Iterator[Header] = headers.iterator

  def get(key: HeaderKey): Option[Header] = headers.find(_.headerKey == key)

  def put(header: Header): Headers = {
    val (pre, post) = headers.span(_.headerKey != header.headerKey)

    if (post.isEmpty) pre :+ header
    else pre ++ (header +: post.tail)
  }

  def ++(headers: immutable.Iterable[Header]): Headers =
    headers.foldLeft(this)(_.put(_))
}

object Headers {
  val empty = apply()

  /** Create a new Headers collection from the headers */
  def apply(headers: Header*): Headers = Headers(headers.toList)

  /** Create a new Headers collection from the headers */
  def apply(headers: List[Header]): Headers = new Headers(headers)

  implicit val canBuildFrom: CanBuildFrom[Traversable[Header], Header, Headers] =
    new CanBuildFrom[TraversableOnce[Header], Header, Headers] {
      def apply(from: TraversableOnce[Header]): mutable.Builder[Header, Headers] = newBuilder

      def apply(): mutable.Builder[Header, Headers] = newBuilder
    }

  private def newBuilder: mutable.Builder[Header, Headers] =
    new mutable.ListBuffer[Header].mapResult(b => new Headers(b))


}
