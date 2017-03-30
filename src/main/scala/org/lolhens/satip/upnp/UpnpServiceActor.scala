package org.lolhens.satip.upnp

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props, Terminated}
import akka.routing.{BroadcastRoutingLogic, Router}
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.model.message.header.STAllHeader
import org.fourthline.cling.model.meta.{LocalDevice, RemoteDevice}
import org.fourthline.cling.registry.{DefaultRegistryListener, Registry}
import org.lolhens.satip.upnp.UpnpServiceActor._
import org.lolhens.satip.upnp.device.UpnpDevice

/**
  * Created by u016595 on 20.03.2017.
  */
class UpnpServiceActor extends Actor {
  var eventRouter = Router(BroadcastRoutingLogic())

  val upnpService = new UpnpServiceImpl(new DefaultRegistryListener {
    override def localDeviceAdded(registry: Registry, localDevice: LocalDevice): Unit =
      self ! DeviceAdded(registry, UpnpDevice(localDevice))

    override def localDeviceRemoved(registry: Registry, localDevice: LocalDevice): Unit =
      self ! DeviceRemoved(registry, UpnpDevice(localDevice))

    override def remoteDeviceAdded(registry: Registry, remoteDevice: RemoteDevice): Unit = {println(remoteDevice.getIdentity().getDescriptorURL + " " + remoteDevice.getDetails().getBaseURL)
      self ! DeviceAdded(registry, UpnpDevice(remoteDevice))}

    override def remoteDeviceRemoved(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! DeviceRemoved(registry, UpnpDevice(remoteDevice))

    override def remoteDeviceUpdated(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! DeviceUpdated(registry, UpnpDevice(remoteDevice))
  })

  def search(): Unit = upnpService.getControlPoint.search(new STAllHeader())

  var upnpDevices: List[UpnpDevice] = Nil

  override def receive: Receive = {
    case Register(ref) =>
      context watch ref
      eventRouter = eventRouter.addRoutee(ref)
      upnpDevices.foreach(ref ! DeviceAdded(upnpService.getRegistry, _))

    case Unregister(ref) =>
      context unwatch ref
      eventRouter = eventRouter.removeRoutee(ref)

    case Terminated(ref) =>
      eventRouter = eventRouter.removeRoutee(ref)

    case Search =>
      search()

    case ListDevices =>
      sender() ! upnpDevices

    case event: Event =>
      event match {
        case DeviceAdded(registry, device) =>
          upnpDevices = device +: upnpDevices

        case DeviceRemoved(registry, device) =>
          upnpDevices = upnpDevices.filterNot(_ == device)

        case _ =>
      }

      eventRouter.route(event, self)
  }

  override def preStart(): Unit = self ! Search

  override def postStop(): Unit = upnpService.shutdown()
}

object UpnpServiceActor {
  val props: Props = Props[UpnpServiceActor]

  def actor(implicit actorSystem: ActorRefFactory): ActorRef = actorSystem.actorOf(props)

  trait Command

  trait Event

  case class Register(actorRef: ActorRef) extends Command

  case class Unregister(actorRef: ActorRef) extends Command

  case object Search extends Command

  case object ListDevices

  case class DeviceAdded(registry: Registry, device: UpnpDevice) extends Event

  case class DeviceRemoved(registry: Registry, device: UpnpDevice) extends Event

  case class DeviceUpdated(registry: Registry, device: UpnpDevice) extends Event

  case class RemoteDeviceDiscoveryStarted(registry: Registry, remoteDevice: RemoteDevice) extends Event

  case class RemoteDeviceDiscoveryFailed(registry: Registry, remoteDevice: RemoteDevice, exception: Exception) extends Event

}
