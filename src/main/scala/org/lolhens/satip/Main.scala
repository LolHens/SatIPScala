package org.lolhens.satip

import java.nio.ByteOrder

import akka.util.{ByteString, ByteStringBuilder}
import org.lolhens.satip.RichByteString._

/**
  * Created by pierr on 22.10.2016.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val builder = new ByteStringBuilder()
    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    builder.putInt(16734)
    println(builder.result().toInt)
  }
}
