package org.lolhens.satip.rtsp2

import java.net.InetSocketAddress

import cats.syntax.show._
import org.http4s.Uri
import org.lolhens.satip.rtsp.data.RtspVersion
import scodec.bits.ByteVector

case class Request(method: Method,
                   uri: Uri,
                   headers: Headers,
                   body: Option[String] = None)
                  (implicit
                   val version: RtspVersion) {
  require(uri.scheme.exists(_.value == "rtsp"), "Uri scheme must be rtsp://")

  def withMethod(method: Method): Request = copy(method = method)
  def withUri(uri: Uri): Request = copy(uri = uri)
  def withHeaders(headers: Headers): Request = copy(headers = headers)
  def withBody(body: Option[String]): Request = copy(body = body)

  def socketAddress: InetSocketAddress =
    uri.authority.map { authority =>
      val host = authority.host.value
      val port = authority.port.getOrElse(Request.defaultPort)
      //InetSocketAddress.createUnresolved(host, port)
      new InetSocketAddress(host, port)
    }.get

  protected[rtsp2] def payload: String = {
    val lines: List[String] =
      List(s"$method ${uri.toString} RTSP/$version") ++
        headers.map(_.show) ++
        List("") ++
        body.toList

    lines.map(_ + "\r\n").mkString
  }

  protected[rtsp2] def binaryPayload: ByteVector =
    ByteVector.encodeUtf8(payload).right.get
}

object Request {
  val defaultPort: Int = 554

  def options(uri: Uri, headers: Headers = Headers.empty)
             (implicit version: RtspVersion): Request =
    Request(Method.Options, uri, headers)

  def describe(uri: Uri, headers: Headers = Headers.empty, body: Option[String])
              (implicit version: RtspVersion): Request =
    Request(Method.Describe, uri, headers, body)

  def setup(uri: Uri, headers: Headers = Headers.empty)
           (implicit version: RtspVersion): Request =
    Request(Method.Setup, uri, headers)

  def play(uri: Uri, headers: Headers = Headers.empty)
          (implicit version: RtspVersion): Request =
    Request(Method.Play, uri, headers)

  def pause(uri: Uri, headers: Headers = Headers.empty)
           (implicit version: RtspVersion): Request =
    Request(Method.Pause, uri, headers)

  def record(uri: Uri, headers: Headers = Headers.empty)
            (implicit version: RtspVersion): Request =
    Request(Method.Record, uri, headers)

  def announce(uri: Uri, headers: Headers = Headers.empty)
              (implicit version: RtspVersion): Request =
    Request(Method.Announce, uri, headers)

  def teardown(uri: Uri, headers: Headers = Headers.empty)
              (implicit version: RtspVersion): Request =
    Request(Method.Teardown, uri, headers)

  def getParameter(uri: Uri, headers: Headers = Headers.empty, body: Option[String])
                  (implicit version: RtspVersion): Request =
    Request(Method.GetParameter, uri, headers, body)

  def setParameter(uri: Uri, headers: Headers = Headers.empty)
                  (implicit version: RtspVersion): Request =
    Request(Method.SetParameter, uri, headers)

  def redirect(uri: Uri, headers: Headers = Headers.empty)
              (implicit version: RtspVersion): Request =
    Request(Method.Redirect, uri, headers)
}
