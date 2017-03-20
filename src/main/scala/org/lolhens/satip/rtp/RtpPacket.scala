package org.lolhens.satip.rtp

import java.nio.ByteOrder

import akka.util.ByteString
import org.lolhens.satip.util.RichByteString._

/**
  * Created by pierr on 23.10.2016.
  */
class RtpPacket(val version: Int,
                val padding: Boolean,
                val extension: Boolean,
                val contributionSourceCount: Int,
                val marker: Boolean,
                val payloadType: Int,
                val sequenceNumber: Int,
                val timestamp: Long,
                val synchronizationSource: Long,
                val contributionSources: List[String],
                val extensionHeaderId: Int = 0,
                val extensionHeaderLength: Int = 0,
                val payload: Option[ByteString] = None) {
  val headerLength: Int = RtpPacket.minHeaderLength

  override def toString: String =
    s"""Version: $version
       |Padding: $padding
       |Extension: $extension
       |Contributing Source Identifiers Count: $contributionSourceCount
       |Marker: $marker
       |Payload Type: $payloadType
       |Sequence Number: $sequenceNumber
       |Timestamp: $timestamp
       |Synchronization Source Identifier: $synchronizationSource""".stripMargin
}

object RtpPacket {
  val minHeaderLength = 12

  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtpPacket = {
    val extension = (byteString(0) & 0x10) != 0
    val contributionSourceCount = byteString(0) & 0x0f

    val extensionBytes = byteString.drop(contributionSourceCount * 4)

    new RtpPacket(
      version = byteString(0) >> 6,
      padding = (byteString(0) & 0x20) != 0,
      extension = extension,
      contributionSourceCount = contributionSourceCount,
      marker = (byteString(1) & 0x80) != 0,
      payloadType = byteString(1) & 0x7f,
      sequenceNumber = byteString.slice(2, 4).toInt,
      timestamp = byteString.slice(4, 8).toLong,
      synchronizationSource = byteString.slice(8, 12).toLong,
      contributionSources = (0 until contributionSourceCount).map { i =>
        byteString.drop(12).slice(i * 4, (i + 1) * 4).mkString
      }.toList,
      extensionHeaderId = if (extension) extensionBytes.slice(0, 2).toInt else 0,
      extensionHeaderLength = if (extension) extensionBytes.slice(2, 4).toInt else 0,
      payload = extensionBytes.drop(if (extension) 4 else 0) match {
        case payloadBytes if payloadBytes.nonEmpty => Some(payloadBytes)
        case _ => None
      }
    )
  }
}