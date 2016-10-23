package org.lolhens.satip.rtcp

import akka.util.ByteString

import scala.collection.immutable.IntMap
import org.lolhens.satip.RichByteString._

/**
  * Created by pierr on 22.10.2016.
  */
class ReportBlock(val synchronizationSource: String,
                  val fractionLost: Int,
                  val cumulativePacketsLost: Int,
                  val highestNumberReceived: Int,
                  val interArrivalJitter: Int,
                  val lastReportTimestamp: Int,
                  val delaySinceLastReport: Int) {
  def length = ReportBlock.length
}

object ReportBlock {
  val length = 24

  def fromByteString(byteString: ByteString): ReportBlock =
    new ReportBlock(
      synchronizationSource = byteString.take(4).mkString,
      fractionLost = byteString(4).toInt,
      cumulativePacketsLost = byteString.slice(5, 8).toInt,
      highestNumberReceived = byteString.slice(8, 12).toInt,
      interArrivalJitter = byteString.slice(12, 16).toInt,
      lastReportTimestamp = byteString.slice(16, 20).toInt,
      delaySinceLastReport = byteString.slice(20, 24).toInt
    )
}