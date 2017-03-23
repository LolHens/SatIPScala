package org.lolhens.satip.satip

import akka.actor.{Actor, ActorRef}
import org.lolhens.satip.upnp.device.DeviceType
import org.lolhens.satip.upnp.{UpnpDevice, UpnpServiceActor}

import scala.xml.XML

/**
  * Created by u016595 on 21.03.2017.
  */
class SatIpDiscoveryActor(upnpService: ActorRef) extends Actor {
  upnpService ! UpnpServiceActor.Register(self)

  override def receive: Receive = {
    case UpnpServiceActor.DeviceAdded(registry, device: UpnpDevice) =>
      device.deviceType match {
        case DeviceType("ses-com", "SatIPServer", version) =>
          println(version)
          println((device.descriptor \ "device").map(_.namespace).mkString(","))
          val ip  = device.descriptorUrl.getHost
          println(ip)

          //println(device.getDetails.getBaseURL)
          println(device.friendlyName)
          //println(device.getDetails.get)
          //println(device.getIdentity.getUdn)
          println(device)
          //println("test")
      }
  }
}


