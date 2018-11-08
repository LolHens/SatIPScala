package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
class RtcpByePacket(val rtcpPacket: RtcpPacket,
                    val synchronizationSources: List[String],
                    val reasonForLeaving: Option[String]) {
  override def toString =
    s"""ByeBye.
       |Version: ${rtcpPacket.version}.
       |Padding: ${rtcpPacket.padding}.
       |Report Count: ${rtcpPacket.reportCount}.
       |Packet Type: ${rtcpPacket.packetType}.
       |Length: ${rtcpPacket.length}.
       |SynchronizationSource: ${synchronizationSources.mkString(", ")}.
       |ReasonForLeaving: ${reasonForLeaving.getOrElse("")}.
       |.""".stripMargin
}

object RtcpByePacket {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtcpByePacket = {
    val rtcpPacketBytes = byteString.take(4)
    val byePacketBytes = byteString.drop(4)

    val rtcpPacket = RtcpPacket.fromByteString(rtcpPacketBytes)
    new RtcpByePacket(
      rtcpPacket = rtcpPacket,
      synchronizationSources = (0 until rtcpPacket.reportCount).map { i =>
        byePacketBytes.slice(i * 4, (i + 1) * 4).mkString
      }.toList,
      reasonForLeaving = {
        val reasonOffset = rtcpPacket.reportCount * 4
        if (reasonOffset < rtcpPacket.length) {
          val reasonBytes = byePacketBytes.drop(reasonOffset)
          val reasonLength = reasonBytes(0).toInt
          Some(reasonBytes.drop(1).take(reasonLength).mkString)
        } else {
          None
        }
      }
    )
  }
}
