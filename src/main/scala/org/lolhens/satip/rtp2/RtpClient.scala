package org.lolhens.satip.rtp2

import java.net.InetSocketAddress

import cats.effect.Blocker
import fs2.io.udp
import fs2.io.udp.Packet
import monix.eval.Task

object RtpClient {
  def socketAddress: InetSocketAddress = new InetSocketAddress("10.1.15.237", 8001)

  def read: Task[Packet] = {
    Blocker[Task].use { blocker =>
      udp.SocketGroup[Task](blocker).use { socketGroup =>
        socketGroup.open[Task](socketAddress).use { socket =>
          //socket.write(Packet(InetSocketAddress.createUnresolved("remote", 1234), Chunk.byteVector(ByteVector.empty)))
          socket.read()
        }
      }
    }
  }
}
