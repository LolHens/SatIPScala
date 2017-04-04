package org.lolhens.satip.rtsp

import java.net.InetSocketAddress

import akka.actor.{ActorRef, ExtendedActorSystem, ExtensionId, ExtensionIdProvider}
import akka.io.{IO, Tcp}

/**
  * Created by pierr on 04.04.2017.
  */
object Rtsp extends ExtensionId[RtspExt] with ExtensionIdProvider {
  override def createExtension(system: ExtendedActorSystem): RtspExt = new RtspExt(system)

  override def lookup = Rtsp

  trait Command

  trait Event

  case class Connect(socketAddress: InetSocketAddress, multicast: Boolean = false) extends Command

  case class CommandFailed(command: Command) extends Event

  case class Connected(remoteAddress: InetSocketAddress, localAddress: InetSocketAddress) extends Event

  case class Received(response: RtspResponse) extends Event

  case class Write(request: RtspRequest) extends Command

  case object ResponseTimeout extends Event

  case object Ack extends Event with Tcp.Event

  type ConnectionClosed = Tcp.ConnectionClosed
}

class RtspExt(system: ExtendedActorSystem) extends IO.Extension {
  override def manager: ActorRef = RtspManager.actor(system)
}
