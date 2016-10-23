package org.lolhens.satip.satip

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

}

object SatIpDevice {
  //def fromUPnPDevice()
}