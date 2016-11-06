package org.lolhens.satip.rtp

import java.net.{InetSocketAddress, MulticastSocket}

import org.lolhens.satip.rtp.RtpListener.TransmissionMode

/**
  * Created by pierr on 04.11.2016.
  */
class MulticastRtpListener(inetSocketAddress: InetSocketAddress) extends RtpListener(inetSocketAddress, TransmissionMode.Multicast) {
  val multicastSocket = new MulticastSocket(inetSocketAddress.getPort)
  multicastSocket.setReuseAddress(false)
  multicastSocket.setSoTimeout(15000)
  multicastSocket.joinGroup(inetSocketAddress.getAddress)

}
