package org.lolhens.satip.satip

import org.fourthline.cling.model.meta.RemoteDevice
import org.lolhens.satip.rtsp.{RtspMethod, RtspRequest}
import org.lolhens.satip.satip.SatIpDevice.Tuner

/**
  * Created by pierr on 23.10.2016.
  */
class SatIpDevice(val baseUrl: String,
                  val friendlyName: String,
                  val uniqueDeviceName: String,
                  val description: String,
                  val capabilities: String,
                  val hasSatteliteBroadcastSupport: Boolean,
                  val hasCableBroadcastSupport: Boolean,
                  val hasTerrestrialBroadcastSupport: Boolean) {
  def checkCapabilities(capabilities: String): Unit = {
    val tunerCount: Map[Tuner, Int] =
      capabilities
        .split(",")
        .map(_.split("-").head.trim.toLowerCase)
        .flatMap(Tuner.valueMap.get)
        .groupBy(e => e)
        .map(e => (e._1, e._2.length)) match {
        case Map.empty =>
          RtspRequest(RtspMethod.describe, s"rtsp://$baseUrl/", 1, 0)
      }


  }
}

object SatIpDevice {
  def fromUPnPDevice(device: RemoteDevice) = {
    ???
  }

  case class Tuner(name: String)

  object Tuner {
    val values = List(dvbs, dvbs2, dvbt, dvbt2, dvbc, dvbc2)

    lazy val valueMap = values.map(e => (e.name, e)).toMap

    val dvbs = Tuner("dvbs")
    val dvbs2 = Tuner("dvbs2")
    val dvbt = Tuner("dvbt")
    val dvbt2 = Tuner("dvbt2")
    val dvbc = Tuner("dvbc")
    val dvbc2 = Tuner("dvbc2")
  }
}