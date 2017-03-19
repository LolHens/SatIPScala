package org.lolhens.satip.rtp

import java.net.InetSocketAddress

import org.lolhens.satip.rtp.RtpListener.TransmissionMode

/**
  * Created by pierr on 23.10.2016.
  */
class RtpListener(val inetSocketAddress: InetSocketAddress,
                  val transmissionMode: TransmissionMode) {
}

object RtpListener {

  class TransmissionMode(val name: String)

  object TransmissionMode {

    object Unicast extends TransmissionMode("unicast")

    object Multicast extends TransmissionMode("multicast")

  }

}