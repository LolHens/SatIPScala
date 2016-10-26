package org.lolhens.satip.upnp

import org.fourthline.cling.model.meta.{Device, RemoteDevice}

/**
  * Created by pierr on 26.10.2016.
  */
class DiscoveredDevice(__device: RemoteDevice) {
  var _device: RemoteDevice = __device // Option

  def device = _device
}

object DiscoveredDevice {
  class Mutator private[upnp](device: RemoteDevice) {
    lazy val discoveredDevice: DiscoveredDevice = new DiscoveredDevice(device)

    def updateDevice(device: RemoteDevice) = discoveredDevice._device = device
  }
}
