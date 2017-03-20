package org.lolhens.satip.upnp

import org.fourthline.cling.model.meta.RemoteDevice

import scala.xml.{Elem, XML}

/**
  * Created by pierr on 20.03.2017.
  */
class RemoteUpnpDevice(remoteDevice: RemoteDevice) extends UpnpDevice {
  override def udn: String = remoteDevice.getIdentity.getUdn.toString

  lazy val descriptor: Elem = XML.load(remoteDevice.getIdentity.getDescriptorURL)

  //val test = remoteDevice.
}
