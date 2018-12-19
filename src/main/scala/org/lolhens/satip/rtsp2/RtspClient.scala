package org.lolhens.satip.rtsp2

import java.net.InetSocketAddress
import java.nio.channels.AsynchronousChannelGroup
import java.util.concurrent.Executors

import cats.effect.Resource
import fs2.Chunk
import fs2.io.tcp
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.http4s.Uri
import org.lolhens.satip.rtsp.data.RtspVersion
import scodec.bits.ByteVector

object RtspClient {

  implicit val rtspVersion = RtspVersion(1, 0)

  val request2 = Request.options(Uri.unsafeFromString(s"rtsp://10.1.15.237/") /*stream=0"*/ , Headers(
    HeaderKey.CSeq(1.toString),
    HeaderKey.Accept("application/sdp") //,
    //RtspHeaderField.Session("0")
  ))

  val connection = Connection(request2.socketAddress)


  val request = connection.send(request2)

  def main(args: Array[String]): Unit = {
    val response = request.runSyncUnsafe()
    println(response)
  }
}
