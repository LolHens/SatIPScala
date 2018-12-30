package org.lolhens.satip.rtsp2

import monix.execution.Scheduler.Implicits.global
import org.http4s.Uri
import org.lolhens.satip.rtp2.RtpClient
import org.lolhens.satip.rtsp.data.RtspVersion

object RtspClient {

  implicit val rtspVersion = RtspVersion(1, 0)

  val request = Request.setup(Uri.unsafeFromString(s"rtsp://10.1.15.237/") /*stream=0"*/ , Headers(
    HeaderKey.CSeq(1.toString),
    HeaderKey.Accept("application/sdp"),
    HeaderKey.Transport.list("RTP/AVP", "unicast", "client_port=8000-8001")
    //RtspHeaderField.Session("0")
  ))

  val connection = Connection(request.socketAddress)


  val responseTask = connection.send(request)

  def main(args: Array[String]): Unit = {
    val r =
      (for {
        response <- responseTask
        a <- RtpClient.read
        _ = println(a)
      } yield
        response)
        .runSyncUnsafe()

    println(r)
  }
}
