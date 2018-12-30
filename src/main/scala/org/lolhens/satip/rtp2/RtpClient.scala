package org.lolhens.satip.rtp2

import java.net.InetSocketAddress
import java.nio.channels.AsynchronousChannelGroup
import java.util.concurrent.Executors

import cats.effect.Resource
import fs2.Chunk
import fs2.io.udp
import fs2.io.udp.{AsynchronousSocketGroup, Packet}
import monix.eval.Task
import org.lolhens.satip.rtsp2.Connection.asyncChannelGroupResource
import monix.execution.Scheduler.Implicits.global
import scodec.bits.ByteVector

class RtpClient {
  val socketAddress = InetSocketAddress.createUnresolved("localhost", 8000)
  RtpClient.asyncSocketGroupResource.use { implicit asyncSocketGroup =>
    val socket = udp.Socket.apply[Task](address = socketAddress)
    socket.use {socket => Task {
      socket.write(Packet(InetSocketAddress.createUnresolved("remote", 1234), Chunk.byteVector(ByteVector.empty)))
      socket.read()
    }}
  }.runSyncUnsafe()
}

object RtpClient {
  private val asyncSocketGroupResource: Resource[Task, AsynchronousSocketGroup] =
    Resource.make[Task, AsynchronousSocketGroup] {
      Task(AsynchronousSocketGroup())
    } { socketGroup =>
      Task(socketGroup.close())
    }
}
