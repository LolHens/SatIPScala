package org.lolhens.satip.upnp.device

import fastparse.NoWhitespace._
import fastparse._
import org.lolhens.satip.util.ParserUtils._

/**
 * Created by u016595 on 21.03.2017.
 */
case class DeviceType(namespace: String, deviceType: String, version: Int) {
  override def toString: String = s"urn:$namespace:device:$deviceType:$version"
}

object DeviceType {
  def apply(deviceType: org.jupnp.model.types.DeviceType): DeviceType =
    DeviceType(deviceType.getNamespace, deviceType.getType, deviceType.getVersion)

  def parser[_: P]: P[DeviceType] =
    ("urn:" ~ (!":" ~ AnyChar).rep.! ~ ":device:" ~ (!":" ~ AnyChar).rep.! ~ ":" ~ digits.!).map {
      case (namespace, deviceType, version) => DeviceType(namespace, deviceType, version.toInt)
    }
}
