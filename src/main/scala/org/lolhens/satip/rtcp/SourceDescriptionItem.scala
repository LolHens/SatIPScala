package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
class SourceDescriptionItem(val `type`: Int,
                            val text: String) {
  def length = text.length + 2
}

object SourceDescriptionItem {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): SourceDescriptionItem =
    new SourceDescriptionItem(
      byteString(0).toInt,
      text = {
        val length = byteString(1).toInt
        byteString.drop(2).take(length).mkString
      }
    )
}