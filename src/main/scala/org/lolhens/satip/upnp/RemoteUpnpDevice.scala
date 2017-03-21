package org.lolhens.satip.upnp

import java.net.URL

import org.fourthline.cling.model.meta.RemoteDevice

import scala.xml.{Elem, XML}

/**
  * Created by pierr on 20.03.2017.
  */
class RemoteUpnpDevice(remoteDevice: RemoteDevice) extends UpnpDevice(remoteDevice.getIdentity.getUdn.toString) {
  def descriptorUrl: URL = remoteDevice.getIdentity.getDescriptorURL

  lazy val descriptor: Elem = XML.load(descriptorUrl)

  override def deviceType: DeviceType = DeviceType(remoteDevice.getType)

  override def friendlyName: String = remoteDevice.getDetails.getFriendlyName
}
