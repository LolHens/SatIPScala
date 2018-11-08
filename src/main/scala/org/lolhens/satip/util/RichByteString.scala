package org.lolhens.satip.util

import java.nio.ByteOrder

import akka.util.ByteString
import org.lolhens.satip.util.RichByteString._

import scala.language.implicitConversions

/**
  * Created by pierr on 22.10.2016.
  */
class RichByteString(val self: ByteString) extends AnyVal {
  def orderedIterator(implicit byteOrder: ByteOrder): Iterator[Byte] =
    if (byteOrder == ByteOrder.BIG_ENDIAN) {
      self.iterator
    } else if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
      self.reverseIterator
    } else throw new IllegalArgumentException("Unknown byte order " + byteOrder)

  def toBigInt(implicit byteOrder: ByteOrder): BigInt =
    orderedIterator.foldLeft(BigInt(0)) { (last, byte) =>
      (last << 8) | BigInt(byte & 0xFF)
    }

  def toLong(implicit byteOrder: ByteOrder): Long =
    self.take(8).orderedIterator.foldLeft(0L) { (last, byte) =>
      (last << 8) | (byte & 0xFF)
    }

  def toInt(implicit byteOrder: ByteOrder): Int = self.take(4).toLong.toInt

  def toShort(implicit byteOrder: ByteOrder): Int = self.take(2).toLong.toShort

  def toByte: Byte = if (self.isEmpty) 0 else self.head
}

object RichByteString {
  implicit def enrichFromByteString(byteString: ByteString): RichByteString = new RichByteString(byteString)
}