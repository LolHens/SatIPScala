package org.lolhens.satip.rtsp

import java.nio.charset.StandardCharsets

import akka.util.ByteString
import org.lolhens.satip.rtsp.data.RtspVersion

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspRequest(method: RtspMethod,
                       uri: String,
                       cSeq: Int,
                       private val _headers: Map[String, String] = Map.empty,
                       body: String = "")
                      (implicit val version: RtspVersion) {
  val headers: Map[String, String] = //cseq + headers

  def toByteString: ByteString = {
    val request =
      s"$method $uri RTSP/$version\r\n${
        headers.map(e => s"${e._1}: ${e._2}\r\n").mkString
      }\r\n$body"

    ByteString.fromString(request, StandardCharsets.UTF_8.name())
  }
}

object RtspRequest {


  def options()(implicit version: RtspVersion): RtspRequest = ???

  def describe()(implicit version: RtspVersion): RtspRequest = ???

  def setup()(implicit version: RtspVersion): RtspRequest = ???

  def play()(implicit version: RtspVersion): RtspRequest = ???

  def pause()(implicit version: RtspVersion): RtspRequest = ???

  def record()(implicit version: RtspVersion): RtspRequest = ???

  def announce()(implicit version: RtspVersion): RtspRequest = ???

  def teardown()(implicit version: RtspVersion): RtspRequest = ???

  def getParameter()(implicit version: RtspVersion): RtspRequest = ???

  def setParameter()(implicit version: RtspVersion): RtspRequest = ???

  def redirect()(implicit version: RtspVersion): RtspRequest = ???
}