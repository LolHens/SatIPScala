package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
class RtcpReceiverReportPacket(val rtcpPacket: RtcpPacket,
                               val synchronizationSource: String,
                               val reportBlocks: List[ReportBlock],
                               val profileExtension: Option[ByteString]) {
  override def toString =
    s"""Receiver Report.
        |Version: ${rtcpPacket.version}.
        |Padding: ${rtcpPacket.padding}.
        |Report Count: ${rtcpPacket.reportCount}.
        |Packet Type: ${rtcpPacket.packetType}.
        |Length: ${rtcpPacket.length}.
        |SynchronizationSource: $synchronizationSource.
        |.""".stripMargin
}

object RtcpReceiverReportPacket {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtcpReceiverReportPacket = {
    val rtcpPacketBytes = byteString.take(4)
    val receiverReportPacketBytes = byteString.drop(4)

    val rtcpPacket = RtcpPacket.fromByteString(rtcpPacketBytes)
    new RtcpReceiverReportPacket(
      rtcpPacket = rtcpPacket,
      synchronizationSource = receiverReportPacketBytes.take(4).mkString,
      reportBlocks = (0 until rtcpPacket.reportCount).map { i =>
        ReportBlock.fromByteString(
          receiverReportPacketBytes.slice(4 + i * ReportBlock.length, 4 + (i + 1) * ReportBlock.length)
        )
      }.toList,
      profileExtension = {
        val profileExtensionOffset = 4 + rtcpPacket.reportCount * ReportBlock.length
        if (profileExtensionOffset < rtcpPacket.length) {
          Some(receiverReportPacketBytes.drop(profileExtensionOffset))
        } else {
          None
        }
      }
    )
  }
}