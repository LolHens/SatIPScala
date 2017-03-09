package org.lolhens.satip.rtsp

import java.net.Socket
import java.nio.ByteOrder

import akka.util.ByteString
import org.lolhens.satip.rtp.RtpListener.TransmissionMode
import org.lolhens.satip.rtp.RtpListener.TransmissionMode.{Multicast, Unicast}
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.satip.SatIpQuery

/**
  * Created by pierr on 23.10.2016.
  */
class RtspSession2(val rtspDevice: RtspDevice,
                   val rtspSessionId: String,
                   val rtspSessionTTL: Int = 0,
                   val rtspStreamId: String,
                   //val rtpSession, rtcpSession
                   val clientRtpPort: Int,
                   val clientRtcpPort: Int,
                   val serverRtpPort: Int,
                   val serverRtcpPort: Int,
                   val rtpPort: Int,
                   val rtcpPort: Int,
                   val rtspStreamUrl: String,
                   val destination: String,
                   val source: String,
                   val transport: String,
                   val signalLevel: Int,
                   val signalQuality: Int,
                   var rtspSocket: Socket = null,
                   var rtspSequenceNum: Int = 1) {
  private var _closed = false

  def closed = _closed

  def close() = {
    _closed = true
  }

  private implicit val rtspVersion = RtspVersion(1, 0)

  def sendRequest(request: RtspRequest) = {
    if (rtspSocket == null) connect()
    val newRequest = request.copy(requestHeaders = request.requestHeaders :+ RtspHeaderField.CSeq(rtspSequenceNum.toString))
    rtspSequenceNum += 1
    val bytes = newRequest.toByteString
    if (rtspSocket != null) {
      val requestBytesCount = 1
      rtspSocket.getOutputStream.write(bytes.toArray)
      if (requestBytesCount < 1) ()
    }
  }

  def receiveResponse: RtspResponse = {
    val bytes = new Array[Byte](1024)
    while (rtspSocket.getInputStream.available() == 0) {
      Thread.sleep(100)
    }
    Thread.sleep(1000)
    val readBytes = rtspSocket.getInputStream.read(bytes)
    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    val response = RtspResponse.fromByteString(ByteString.fromArray(bytes, 0, readBytes))
    val contentLength = response.entity.flatMap(_.entityHeaders.find(_.headerField == RtspHeaderField.ContentLength)).map(_.value.toInt)
    println(contentLength)
    contentLength.map { length =>
      val bytes = new Array[Byte](length)
      val readBytes = rtspSocket.getInputStream.read(bytes)
      val byteString = ByteString.fromArray(bytes, 0, readBytes)
      val body = byteString.utf8String
      println(body)
      body
    }

    response
  }

  def setup(query: String, transmissionMode: TransmissionMode) = {
    //: RtspStatusCode = {
    val headers: List[RtspHeaderField.RequestField#Value] = transmissionMode match {
      case Multicast =>
        List(RtspHeaderField.Transport(s"RTP/AVP;${transmissionMode.name.toLowerCase}"))
      case Unicast =>
        def find2FreeTcpPorts: (Int, Int) = (5555, 5556)

        val (clientRtpPort, clientRtcpPort) = find2FreeTcpPorts
        List(RtspHeaderField.Transport(s"RTP/AVP;${transmissionMode.name.toLowerCase};client_port=$clientRtpPort-$clientRtcpPort"))
    }

    val request: RtspRequest = RtspRequest.setup(s"rtsp://${rtspDevice.serverAddress}:${554}/?$query", 1 /*CSeq*/ , headers)
    sendRequest(request)
    receiveResponse
    //val response: RtspResponse = ???
    //???
    //request.
  }

  def connect() = {
    rtspSocket = new Socket(rtspDevice.serverAddress, 554)
  }

  def describe() = {
    rtspSocket = new Socket("192.168.1.5", 554)
    val request = RtspRequest.describe(s"rtsp://192.168.1.5:554/stream=0", 0, List(
      RtspHeaderField.Accept("application/sdp"),
      RtspHeaderField.Session("0")
    ), RtspEntity(Nil, ""))
    sendRequest(request)
    receiveResponse
  }
}

object RtspSession2 {
  def test = {
    val session = new RtspSession(RtspDevice("192.168.1.5", "uuid:00000000-0000-1000-8f62-00059e979f48", "Triax SatIP Converter"), "",
      0,
      "",
      0, 0, 0, 0, 0, 0,
      "", "", "",
      "", 0, 0)

    import org.lolhens.satip.satip.SatIpQuery.Parameter._
    val query = SatIpQuery(
      FrontendIdentifier(1),
      SignalSource(),
      ModulationSystem.dvbs,
      Frequency(12545),
      Polarisation.HorizontalLinear,
      SymbolRate(22000),
      FECInner(56),
      ModulationType.qpsk,
      ListPIDs(PID.Num(0))
    )

    session.setup(query.buildQueryString, TransmissionMode.Unicast)
    //session.describe()
  }

  import fastparse.all._
  import org.lolhens.satip.util.ParserUtils._

  val defaultRtspSessionTTL = 30 // seconds

  val rtspSessionHeaderParser: Parser[(String, Int)] =
    P(s ~ (!(space | ";")).rep(min = 1).! ~ (";timeout=" ~ digits.!.map(_.toInt)).?).map {
      case (rtspSessionId, rtspSessionTTL) => (rtspSessionId, rtspSessionTTL.getOrElse(defaultRtspSessionTTL))
    }

  val describeResponseSignalInfo: Parser[(Boolean, Double, Double)] =
    P(";tuner=" ~ digits ~ "," ~ digits.!.map(_.toInt) ~ "," ~ digits.!.map(_.toInt) ~ "," ~ digits.!.map(_.toInt) ~ ",").map {
      case (level, signalLocked, quality) =>
        (signalLocked == 1, level.toDouble * 100 / 255, quality.toDouble * 100 / 255)
    }
}
