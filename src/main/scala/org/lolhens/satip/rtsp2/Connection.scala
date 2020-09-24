package org.lolhens.satip.rtsp2

import java.net.InetSocketAddress
import java.nio.channels.AsynchronousChannelGroup
import java.util.concurrent.Executors

import cats.effect.{Blocker, Resource}
import fs2.Chunk
import fs2.io.tcp
import monix.eval.Task
import monix.execution.atomic.{Atomic, AtomicLong}
import org.lolhens.satip.rtsp2.Connection._
import scodec.bits.ByteVector

case class Connection(address: InetSocketAddress) {
  private val nextCSeq: AtomicLong = Atomic(1L)

  def send(request: Request,
           cSeq: Long = nextCSeq.getAndIncrement()): Task[String] = {
    val newRequest = request.withHeaders(request.headers.put(HeaderKey.CSeq(cSeq.toString)))

    Blocker[Task].use { blocker =>
      tcp.SocketGroup[Task](blocker).use { socketGroup =>
        socketGroup.client[Task](address).use { socket =>
          val readAll = socket.reads(4096).chunks.map(byteVectorFromChunk).fold(ByteVector.empty)(_ ++ _).compile.toList.map(_.head)

          for {
            _ <- socket.write(Chunk.byteVector(newRequest.binaryPayload))
            bytes <- socket.read(1024 * 1024).map(_.map(byteVectorFromChunk).getOrElse(ByteVector.empty)) //readAll
            string = bytes.decodeUtf8.toTry.get
          } yield
            string
        }
      }
    }
  }
}

object Connection {
  private def byteVectorFromChunk(chunk: Chunk[Byte]): ByteVector = chunk match {
    case byteVectorChunk: Chunk.ByteVectorChunk => byteVectorChunk.toByteVector
    case byteBufferChunk: Chunk.ByteBuffer => ByteVector(byteBufferChunk.buf)
    case _ => ByteVector(chunk.toArray)
  }

  private val asyncChannelGroupResource: Resource[Task, AsynchronousChannelGroup] =
    Resource.make[Task, AsynchronousChannelGroup] {
      Task {
        val numThreads = Runtime.getRuntime.availableProcessors() * 2 + 2
        AsynchronousChannelGroup.withFixedThreadPool(numThreads, Executors.defaultThreadFactory())
      }
    } { channelGroup =>
      Task(channelGroup.shutdownNow())
    }
}
