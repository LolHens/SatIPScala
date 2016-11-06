package org.lolhens.satip.rtp

import java.net.{DatagramSocket, InetSocketAddress}

import org.lolhens.satip.rtp.RtpListener.TransmissionMode

/**
  * Created by pierr on 04.11.2016.
  */
class UnicastRtpListener(inetSocketAddress: InetSocketAddress) extends RtpListener(inetSocketAddress, TransmissionMode.Unicast) {
  val datagramSocket = new DatagramSocket()

}
