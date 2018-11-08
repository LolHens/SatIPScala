package org.lolhens.satip.rtsp

import java.net.Socket
import java.nio.ByteOrder
import java.util.concurrent.locks.ReentrantLock

import akka.util.ByteStringBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by pierr on 23.10.2016.
  */
class RtspClient(val serverHost: String) {
  private val lock = new ReentrantLock()

  private val client = new Socket(serverHost, 554)

  @volatile private var cseq: Int = 1

  def request(request: RtspRequest): Future[RtspResponse] = Future {
    lock.lock()

    if (client.getInputStream.available() > 0)
      throw new RuntimeException("Unexpected bytes in buffer!")

    val bytes = request
      .copy(requestHeaders =
        request.requestHeaders :+ RtspHeaderField.CSeq(cseq))(request.version)
      .toByteString

    client.getOutputStream.write(bytes.toArray)

    cseq += 1

    // wait for response
    val builder = new ByteStringBuilder()

    val bufferSize = client.getReceiveBufferSize
    val buffer = new Array[Byte](bufferSize)

    while ( {
      val numBytes = client.getInputStream.read(buffer)
      builder.putBytes(buffer, 0, numBytes)
      client.getInputStream.available() > 0
    }) ()

    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    val response = RtspResponse.fromByteString(builder.result())

    // TODO: maybe read full body

    lock.unlock()
    response
  }

  def close() = {
    lock.lock()
    client.close()
    lock.unlock()
  }
}
