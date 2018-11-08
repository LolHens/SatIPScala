package org.lolhens.satip.rtsp

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props, Stash, Terminated}
import akka.io.Tcp
import akka.routing.{BroadcastRoutingLogic, Router}
import org.lolhens.satip.rtsp.Rtsp._
import org.lolhens.satip.rtsp.RtspOutgoingConnection.{Register, Unregister}

/**
  * Created by pierr on 03.04.2017.
  */
private[rtsp] class RtspOutgoingConnection(tcpConnection: ActorRef) extends Actor with Stash {
  var eventRouter = Router(BroadcastRoutingLogic())

  tcpConnection ! Tcp.Register(self, keepOpenOnPeerClosed = true, useResumeWriting = false)

  override def receive: Receive = {
    case Register(ref) =>
      context watch ref
      eventRouter = eventRouter.addRoutee(ref)

    case Unregister(ref) =>
      context unwatch ref
      eventRouter = eventRouter.removeRoutee(ref)

    case Terminated(ref) =>
      eventRouter = eventRouter.removeRoutee(ref)

    case write@Write(request) =>
      val listener = sender()

      tcpConnection ! Tcp.Write({
        val data = request.toByteString
        data
      }, Ack)

      context become {
        case Ack =>
          listener ! Ack
          unstashAll()
          context.unbecome()

        case Tcp.CommandFailed(_: Tcp.Write) =>
          listener ! CommandFailed(write)
          unstashAll()
          context.unbecome()

        case _: ConnectionClosed =>
          stash()
          unstashAll()
          context.unbecome()

        case _ => stash()
      }

    case Tcp.Received(data) =>
      eventRouter.route(Received {
        val response = RtspResponse.fromByteString(data)
        response
      }, self)

    case closed: ConnectionClosed =>
      eventRouter.route(closed, self)
      context stop self
  }
}

object RtspOutgoingConnection {
  private[rtsp] def props(tcpConnection: ActorRef): Props =
    Props(new RtspOutgoingConnection(tcpConnection))

  private[rtsp] def actor(tcpConnection: ActorRef)
                         (implicit actorRefFactory: ActorRefFactory): ActorRef =
    actorRefFactory.actorOf(props(tcpConnection), "RTSP-Outgoing-Connection")

  case class Register(actorRef: ActorRef) extends Command

  case class Unregister(actorRef: ActorRef) extends Command

}
