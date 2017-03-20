package org.lolhens.satip.upnp

import org.fourthline.cling.model.meta.LocalDevice

import scala.xml.XML

/**
  * Created by pierr on 20.03.2017.
  */
class LocalUpnpDevice(localDevice: LocalDevice) extends UpnpDevice {
  override def udn: String = localDevice.getIdentity.getUdn.toString
}
