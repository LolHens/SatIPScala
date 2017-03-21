package org.lolhens.satip.upnp

/**
  * Created by u016595 on 21.03.2017.
  */
case class DeviceType(namespace: String, `type`: String, version: Int)

object DeviceType {
  def apply(deviceType: org.fourthline.cling.model.types.DeviceType): DeviceType =
    DeviceType(deviceType.getNamespace, deviceType.getType, deviceType.getVersion)
}
