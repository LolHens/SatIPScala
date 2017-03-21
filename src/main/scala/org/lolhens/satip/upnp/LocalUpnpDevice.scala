package org.lolhens.satip.upnp

import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.types.DeviceType

import scala.xml.XML

/**
  * Created by pierr on 20.03.2017.
  */
class LocalUpnpDevice(localDevice: LocalDevice) extends UpnpDevice(localDevice.getIdentity.getUdn.toString) {
  override def deviceType: DeviceType = DeviceType(localDevice.getType)

  override def friendlyName: String = localDevice.getDetails.getFriendlyName
}
