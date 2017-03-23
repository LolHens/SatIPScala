package org.lolhens.satip.satip

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}
import org.lolhens.satip.upnp.UpnpServiceActor
import org.lolhens.satip.upnp.device.{DeviceType, UpnpDevice}

/**
  * Created by u016595 on 21.03.2017.
  */
class SatIpDiscoveryActor(upnpService: ActorRef) extends Actor {
  upnpService ! UpnpServiceActor.Register(self)

  override def receive: Receive = {
    case UpnpServiceActor.DeviceAdded(registry, device: UpnpDevice) =>
      device.deviceType match {
        case DeviceType("ses-com", "SatIPServer", version) =>
          device.remoteDetails match {
            case Some(remoteDetails) =>
              println(version)
              println((remoteDetails.descriptor \ "device").map(_.namespace).mkString(","))
              val ip = remoteDetails.remoteInterfaceAddress
              println(ip)

              //println(device.getDetails.getBaseURL)
              println(device.friendlyName)
              //println(device.getDetails.get)
              //println(device.getIdentity.getUdn)
              println(device)
            //println("test")

            case None =>
          }

        case _ =>
      }
  }
}

object SatIpDiscoveryActor {
  def props(upnpService: ActorRef) =
    Props(classOf[SatIpDiscoveryActor], upnpService)

  def actor(upnpService: ActorRef = null)(implicit actorSystem: ActorRefFactory) =
    actorSystem.actorOf(props(Option(upnpService).getOrElse(UpnpServiceActor.actor)))
}


