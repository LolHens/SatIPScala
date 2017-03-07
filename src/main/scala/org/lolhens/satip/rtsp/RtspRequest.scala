package org.lolhens.satip.rtsp

import java.nio.charset.StandardCharsets

import akka.util.ByteString
import org.lolhens.satip.rtsp.data.RtspVersion

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspRequest(method: RtspMethod,
                       uri: String,
                       requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil,
                       entity: Option[RtspEntity] = None)
                      (implicit val version: RtspVersion) {
  private def headers = requestHeaders ++ entity.map(_.entityHeaders).getOrElse(Nil)

  private def body = entity.map(_.body).getOrElse("")

  def request: String =
    s"$method $uri RTSP/$version\r\n${
      headers.map(e => s"${e.headerField.name}: ${e.value}\r\n").mkString
    }\r\n$body"

  def toByteString: ByteString =
    ByteString.fromString(request, StandardCharsets.UTF_8.name())
}

object RtspRequest {
  def options(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def describe(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil, entity: RtspEntity)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders, Some(entity))

  def setup(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Setup, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def play(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def pause(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def record(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def announce(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def teardown(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def getParameter(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil, entity: RtspEntity)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders, Some(entity))

  def setParameter(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)

  def redirect(uri: String, cSeq: Int, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)(implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, List(RtspHeaderField.CSeq(cSeq.toString)) ++ requestHeaders)
}