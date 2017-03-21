package org.lolhens.satip.upnp

import org.fourthline.cling.model.types.DeviceType

import scala.xml.Elem

/**
  * Created by pierr on 20.03.2017.
  */
abstract case class UpnpDevice(udn: String) {
  def deviceType: DeviceType

  def friendlyName: String
}
