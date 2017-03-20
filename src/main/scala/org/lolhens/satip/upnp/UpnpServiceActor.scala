package org.lolhens.satip.upnp

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing.{BroadcastRoutingLogic, Router}
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.model.message.header.STAllHeader
import org.fourthline.cling.model.meta.{LocalDevice, RemoteDevice}
import org.fourthline.cling.registry.{Registry, RegistryListener}
import org.lolhens.satip.upnp.UpnpServiceActor._

/**
  * Created by u016595 on 20.03.2017.
  */
class UpnpServiceActor extends Actor {
  val upnpService = new UpnpServiceImpl(new RegistryListener {
    override def localDeviceAdded(registry: Registry, localDevice: LocalDevice): Unit =
      self ! LocalDeviceAdded(registry, localDevice)

    override def localDeviceRemoved(registry: Registry, localDevice: LocalDevice): Unit =
      self ! LocalDeviceRemoved(registry, localDevice)

    override def remoteDeviceAdded(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! RemoteDeviceAdded(registry, remoteDevice)

    override def remoteDeviceRemoved(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! RemoteDeviceRemoved(registry, remoteDevice)

    override def remoteDeviceUpdated(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! RemoteDeviceUpdated(registry, remoteDevice)

    override def remoteDeviceDiscoveryStarted(registry: Registry, remoteDevice: RemoteDevice): Unit =
      self ! RemoteDeviceDiscoveryStarted(registry, remoteDevice)

    override def remoteDeviceDiscoveryFailed(registry: Registry, remoteDevice: RemoteDevice, exception: Exception): Unit =
      self ! RemoteDeviceDiscoveryFailed(registry, remoteDevice, exception)

    override def beforeShutdown(registry: Registry): Unit = ()

    override def afterShutdown(): Unit = ()
  })

  var eventRouter = Router(BroadcastRoutingLogic())

  override def receive: Receive = {
    case Register(ref) =>
      context watch ref
      eventRouter = eventRouter.addRoutee(ref)

    case Terminated(ref) =>
      eventRouter = eventRouter.removeRoutee(ref)

    case Search =>
      upnpService.getControlPoint.search(new STAllHeader())

    case ListLocalDevices =>
      sender() ! upnpService.getRegistry.getLocalDevices

    case ListRemoteDevices =>
      sender() ! upnpService.getRegistry.getRemoteDevices

    case event: Event =>
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

  case object ListLocalDevices

  case object ListRemoteDevices

  case class LocalDeviceAdded(registry: Registry, localDevice: LocalDevice) extends Event

  case class LocalDeviceRemoved(registry: Registry, localDevice: LocalDevice) extends Event

  case class RemoteDeviceAdded(registry: Registry, remoteDevice: RemoteDevice) extends Event

  case class RemoteDeviceRemoved(registry: Registry, remoteDevice: RemoteDevice) extends Event

  case class RemoteDeviceUpdated(registry: Registry, remoteDevice: RemoteDevice) extends Event

  case class RemoteDeviceDiscoveryStarted(registry: Registry, remoteDevice: RemoteDevice) extends Event

  case class RemoteDeviceDiscoveryFailed(registry: Registry, remoteDevice: RemoteDevice, exception: Exception) extends Event

}
