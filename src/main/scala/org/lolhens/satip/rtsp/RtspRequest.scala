package org.lolhens.satip.rtsp

import java.nio.charset.StandardCharsets

import akka.http.scaladsl.model.Uri
import akka.util.ByteString
import org.lolhens.satip.rtsp.data.RtspVersion

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspRequest(method: RtspMethod,
                       uri: Uri,
                       requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil,
                       entity: Option[RtspEntity] = None)
                      (implicit val version: RtspVersion) {
  private def headers = requestHeaders ++ entity.toList.flatMap(_.entityHeaders)

  private def body = entity.map(_.body).getOrElse("")

  def request: String = (
    List(s"$method ${uri.toString} RTSP/$version") ++
      headers.map(e => s"${e.headerField.name}: ${e.value}") ++
      List("", body)
    )
    .mkString("\r\n")

  def toByteString: ByteString =
    ByteString.fromString(request, StandardCharsets.UTF_8.name())
}

object RtspRequest {
  def options(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
             (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Options, uri, requestHeaders)

  def describe(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil, entity: RtspEntity)
              (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Describe, uri, requestHeaders, Some(entity))

  def setup(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
           (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Setup, uri, requestHeaders)

  def play(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
          (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Play, uri, requestHeaders)

  def pause(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
           (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Pause, uri, requestHeaders)

  def record(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
            (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Record, uri, requestHeaders)

  def announce(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
              (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Announce, uri, requestHeaders)

  def teardown(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
              (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Teardown, uri, requestHeaders)

  def getParameter(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil, entity: RtspEntity)
                  (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.GetParameter, uri, requestHeaders, Some(entity))

  def setParameter(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
                  (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.SetParameter, uri, requestHeaders)

  def redirect(uri: Uri, requestHeaders: List[RtspHeaderField.RequestField#Value] = Nil)
              (implicit version: RtspVersion): RtspRequest =
    RtspRequest(RtspMethod.Redirect, uri, requestHeaders)
}