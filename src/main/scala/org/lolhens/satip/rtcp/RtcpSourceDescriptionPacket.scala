package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
class RtcpSourceDescriptionPacket(val rtcpPacket: RtcpPacket,
                                  val descriptions: List[SourceDescriptionBlock]) {
  override def toString =
    s"""Receiver Report.
       |Version: ${rtcpPacket.version}.
       |Padding: ${rtcpPacket.padding}.
       |Report Count: ${rtcpPacket.reportCount}.
       |Packet Type: ${rtcpPacket.packetType}.
       |Length: ${rtcpPacket.length}.
       |Descriptions: ${descriptions.mkString(", ")}.
       |.""".stripMargin
}

object RtcpSourceDescriptionPacket {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtcpSourceDescriptionPacket = {
    val rtcpPacket = RtcpPacket.fromByteString(byteString.take(4))
    new RtcpSourceDescriptionPacket(
      rtcpPacket = rtcpPacket,
      descriptions = (0 until rtcpPacket.reportCount).foldLeft((byteString.drop(4), Nil: List[SourceDescriptionBlock]))((last, _) => last match {
        case (byteString, descriptions) =>
          val description = SourceDescriptionBlock.fromByteString(byteString)
          (byteString.drop(description.length), descriptions :+ description)
      })._2
    )
  }
}