package org.lolhens.satip.rtp2

import java.net.InetSocketAddress

import cats.effect.Resource
import fs2.io.udp
import fs2.io.udp.{AsynchronousSocketGroup, Packet}
import monix.eval.Task

object RtpClient {
  def socketAddress: InetSocketAddress = new InetSocketAddress("10.1.15.237", 8000)

  def read: Task[Packet] =
    RtpClient.asyncSocketGroupResource.use { implicit asyncSocketGroup =>
      val socket = udp.Socket.apply[Task](address = socketAddress)
      socket.use { socket =>
        //socket.write(Packet(InetSocketAddress.createUnresolved("remote", 1234), Chunk.byteVector(ByteVector.empty)))
        socket.read()
      }
    }

  private val asyncSocketGroupResource: Resource[Task, AsynchronousSocketGroup] =
    Resource.make[Task, AsynchronousSocketGroup] {
      Task(AsynchronousSocketGroup())
    } { socketGroup =>
      Task(socketGroup.close())
    }
}
