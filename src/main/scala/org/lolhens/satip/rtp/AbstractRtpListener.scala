package org.lolhens.satip.rtp

import java.net.{DatagramSocket, InetSocketAddress}

import org.lolhens.satip.rtp.RtpListener.TransmissionMode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by pierr on 04.11.2016.
  */
class AbstractRtpListener(inetSocketAddress: InetSocketAddress,
                          transmissionMode: TransmissionMode,
                          val socket: DatagramSocket) extends RtpListener(inetSocketAddress, transmissionMode) {
  Future {

  }
}
