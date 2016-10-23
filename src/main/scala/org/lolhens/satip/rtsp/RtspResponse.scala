package org.lolhens.satip.rtsp

import java.nio.ByteOrder

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspResponse(majorVersion: Int = 1,
                        minorVersion: Int,
                        statusCode: RtspStatusCode,
                        reasonPhrase: String,
                        headers: Map[String, String],
                        body: String)

object RtspResponse {
  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtspResponse = {
    val responseString = byteString.utf8String

    ???
  }
}
