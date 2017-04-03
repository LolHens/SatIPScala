package org.lolhens.satip.rtsp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorRefFactory, Cancellable, Kill, Props, Stash, Terminated}
import akka.io.{IO, Tcp}
import akka.pattern.ask
import akka.routing.{BroadcastRoutingLogic, Router}
import akka.util.Timeout
import org.lolhens.satip.rtsp.RtspActor._
import org.lolhens.satip.rtsp.data.RtspVersion

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by pierr on 27.03.2017.
  */
class RtspActor extends Actor with Stash {
  override def receive: Receive = {
    case connect@Connect(socketAddress, multicast) =>
      val listener = sender()

      IO(Tcp)(context.system) ! Tcp.Connect(socketAddress)

      context become {
        case Tcp.Connected(remoteAddress, localAddress) =>
          val tcpConnection = sender()
          val connection = RtspConnectionActor.actor(tcpConnection)
          val requester = RtspRequestActor.actor(connection)

          listener tell(Connected(remoteAddress, localAddress), requester)
          tcpConnection ! Tcp.Register(connection, keepOpenOnPeerClosed = true, useResumeWriting = false)

          unstashAll()
          context.unbecome()

        case Tcp.CommandFailed(_: Tcp.Connect) =>
          listener ! CommandFailed(connect)

          unstashAll()
          context.unbecome()

        case _ => stash()
      }
  }
}

object RtspActor {
  val props: Props = Props[RtspActor]

  def actor(implicit actorRefFactory: ActorRefFactory): ActorRef =
    actorRefFactory.actorOf(props, "RtspManager")

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
