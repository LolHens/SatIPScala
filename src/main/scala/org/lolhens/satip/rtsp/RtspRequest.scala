package org.lolhens.satip.rtsp

import java.nio.charset.StandardCharsets

import akka.util.ByteString

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspRequest(method: RtspMethod,
                       uri: String,
                       majorVersion: Int,
                       minorVersion: Int,
                       headers: Map[String, String],
                       body: String = "") {
  def toByteString: ByteString = {
    val request = s"$method $uri $majorVersion $minorVersion\r\n${
      headers.map(e => s"${e._1}: ${e._2}\r\n").mkString
    }\r\n$body"
    // TODO: log request
    ByteString.fromString(request, StandardCharsets.UTF_8.name())
  }
}
