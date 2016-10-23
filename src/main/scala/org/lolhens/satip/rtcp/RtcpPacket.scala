package org.lolhens.satip.rtcp

import java.nio.ByteOrder

import akka.util.ByteString
import org.lolhens.satip.RichByteString._

/**
  * Created by pierr on 22.10.2016.
  */
class RtcpPacket(val version: Int,
                 val padding: Boolean,
                 val reportCount: Int,
                 val packetType: Int,
                 val length: Int)

object RtcpPacket {
  // 4 Bytes
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtcpPacket =
    new RtcpPacket(
      version = byteString(0) >> 6,
      padding = (byteString(0) & 0x20) != 0,
      reportCount = byteString(0) & 0x1f,
      packetType = byteString(1),
      length = byteString.slice(2, 4).toInt * 4 + 4
    )
}
