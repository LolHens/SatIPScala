package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString
import org.lolhens.satip.RichByteString._

/**
  * Created by pierr on 23.10.2016.
  */
class RtcpSenderReportPacket(val rtcpPacket: RtcpPacket,
                             val synchronizationSource: Int,
                             val nptTimestamp: Long,
                             val rtpTimestamp: Int,
                             val senderPacketCount: Int,
                             val senderOctetCount: Int,
                             val reportBlocks: List[ReportBlock],
                             val profileExtension: Option[ByteString]) {
  override def toString =
    s"""Sender Report.
        |Version: ${rtcpPacket.version}.
        |Padding: ${rtcpPacket.padding}.
        |Report Count: ${rtcpPacket.reportCount}.
        |Packet Type: ${rtcpPacket.packetType}.
        |Length: ${rtcpPacket.length}.
        |SynchronizationSource: $synchronizationSource.
        |NTP Timestamp: ${nptTimestamp /*TODO*/}.
        |RTP Timestamp: $rtpTimestamp.
        |Sender Packet Count: $senderPacketCount.
        |Sender Octet Count: $senderOctetCount.
        |.""".stripMargin
}

object RtcpSenderReportPacket {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtcpSenderReportPacket = {
    val rtcpPacketBytes = byteString.take(4)
    val receiverReportPacketBytes = byteString.drop(4)

    val rtcpPacket = RtcpPacket.fromByteString(rtcpPacketBytes)
    new RtcpSenderReportPacket(
      rtcpPacket = rtcpPacket,
      synchronizationSource = receiverReportPacketBytes.take(4).toInt,
      nptTimestamp = receiverReportPacketBytes.slice(4, 12).toLong,
      rtpTimestamp = receiverReportPacketBytes.slice(12, 16).toInt,
      senderPacketCount = receiverReportPacketBytes.slice(16, 20).toInt,
      senderOctetCount = receiverReportPacketBytes.slice(20, 24).toInt,
      reportBlocks = (0 until rtcpPacket.reportCount).map { i =>
        ReportBlock.fromByteString(
          receiverReportPacketBytes.slice(24 + i * ReportBlock.length, 24 + (i + 1) * ReportBlock.length)
        )
      }.toList,
      profileExtension = {
        val profileExtensionOffset = 24 + rtcpPacket.reportCount * ReportBlock.length
        if (profileExtensionOffset < rtcpPacket.length) {
          Some(receiverReportPacketBytes.drop(profileExtensionOffset))
        } else {
          None
        }
      }
    )
  }
}