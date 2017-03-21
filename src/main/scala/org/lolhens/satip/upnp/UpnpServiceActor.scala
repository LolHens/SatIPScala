package org.lolhens.satip.upnp

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing.{BroadcastRoutingLogic, Router}
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.model.message.header.STAllHeader
import org.fourthline.cling.model.meta.{LocalDevice, RemoteDevice}
import org.fourthline.cling.registry.{DefaultRegistryListener, Registry}
import org.lolhens.satip.upnp.UpnpServiceActor._

/**
  * Created by u016595 on 20.03.2017.
  */
class UpnpServiceActor extends Actor {
  val upnpService = new UpnpServiceImpl(new DefaultRegistryListener {
    override def localDeviceAdded(registry: Registry, localDevice: LocalDevice): Unit =
      self ! DeviceAdded(registry, new LocalUpnpDevice(localDevice))

    override def localDeviceRemoved(registry: Registry, localDevice: LocalDevice): Unit =
      self ! DeviceRemoved(registry, new LocalUpnpDevice(localDevice))

    override def remoteDeviceAdded(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! DeviceAdded(registry, new RemoteUpnpDevice(remoteDevice))

    override def remoteDeviceRemoved(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! DeviceRemoved(registry, new RemoteUpnpDevice(remoteDevice))

    override def remoteDeviceUpdated(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! DeviceUpdated(registry, new RemoteUpnpDevice(remoteDevice))
  })

  var eventRouter = Router(BroadcastRoutingLogic())

  def search(): Unit = upnpService.getControlPoint.search(new STAllHeader())

  var upnpDevices: List[UpnpDevice] = Nil

  override def receive: Receive = {
    case Register(ref) =>
      context watch ref
      eventRouter = eventRouter.addRoutee(ref)
      upnpDevices.foreach(ref ! DeviceAdded(upnpService.getRegistry, _))

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
      }

      eventRouter.route(event, self)
  }

  override def preStart(): Unit = self ! Search

  override def postStop(): Unit = upnpService.shutdown()
}

object UpnpServiceActor {
  val props: Props = Props[UpnpServiceActor]

  def actor(implicit actorSystem: ActorSystem): ActorRef = actorSystem.actorOf(props)

  trait Command

  trait Event

  case class Register(actorRef: ActorRef) extends Command

  case object Search extends Command

  case object ListDevices

  case class DeviceAdded(registry: Registry, device: UpnpDevice) extends Event

  case class DeviceRemoved(registry: Registry, device: UpnpDevice) extends Event

  case class DeviceUpdated(registry: Registry, device: RemoteUpnpDevice) extends Event

  case class RemoteDeviceDiscoveryStarted(registry: Registry, remoteDevice: RemoteDevice) extends Event

  case class RemoteDeviceDiscoveryFailed(registry: Registry, remoteDevice: RemoteDevice, exception: Exception) extends Event

}
