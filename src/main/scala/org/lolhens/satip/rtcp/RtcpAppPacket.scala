package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString
import org.lolhens.satip.RichByteString._

/**
  * Created by pierr on 22.10.2016.
  */
class RtcpAppPacket(val rtcpPacket: RtcpPacket,
                    val synchronizationSource: Int,
                    val name: String,
                    val identity: Int,
                    val data: Option[String]) {
  override def toString =
    s"""Application Specific.
        |Version: ${rtcpPacket.version}.
        |Padding: ${rtcpPacket.padding}.
        |Report Count: ${rtcpPacket.reportCount}.
        |Packet Type: ${rtcpPacket.packetType}.
        |Length: ${rtcpPacket.length}.
        |SynchronizationSource: $synchronizationSource.
        |Name: $name.
        |Identity: $identity.
        |Data: ${data.getOrElse("")}.
        |.""".stripMargin
}

object RtcpAppPacket {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtcpAppPacket = {
    val rtcpPacketBytes = byteString.take(4)
    val appPacketBytes = byteString.drop(4)

    new RtcpAppPacket(
      rtcpPacket = RtcpPacket.fromByteString(rtcpPacketBytes),
      synchronizationSource = appPacketBytes.take(4).toInt,
      name = appPacketBytes.slice(4, 8).mkString,
      identity = appPacketBytes.slice(8, 10).toInt,
      data = {
        val dataLength = appPacketBytes.slice(10, 12).toInt
        if (dataLength > 0)
          Some(appPacketBytes.drop(12).take(dataLength).mkString)
        else
          None
      }
    )
  }
}
