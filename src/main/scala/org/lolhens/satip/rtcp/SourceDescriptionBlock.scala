package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
class SourceDescriptionBlock(val synchronizationSource: String,
                             val items: List[SourceDescriptionItem],
                             blockLength: Int) {
  def length = blockLength + (blockLength % 4)
}

object SourceDescriptionBlock {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): SourceDescriptionBlock = {
    def nextItems(byteString: ByteString): List[SourceDescriptionItem] = {
      val item = SourceDescriptionItem.fromByteString(byteString)
      if (item.`type` != 0)
        item +: nextItems(byteString.drop(item.length))
      else
        Nil
    }

    val items = nextItems(byteString.drop(4))

    new SourceDescriptionBlock(
      synchronizationSource = byteString.take(4).mkString,
      items = items,
      blockLength = items.foldLeft(1)((last, e) => last + e.length)
    )
  }
}