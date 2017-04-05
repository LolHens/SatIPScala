package org.lolhens.satip.rtsp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props, Stash}
import akka.pattern.ask
import akka.util.Timeout
import org.lolhens.satip.rtsp.Rtsp._
import org.lolhens.satip.rtsp.RtspManager._
import org.lolhens.satip.rtsp.RtspSessionActor.KeepAlive
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.util.ContextScheduler

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by pierr on 03.04.2017.
  */
private[rtsp] class RtspSessionActor(tcpConnection: ActorRef, remoteAddress: InetSocketAddress) extends Actor with Stash with ContextScheduler {
  val outgoingConnection: ActorRef = RtspOutgoingConnection.actor(tcpConnection)

  outgoingConnection ! RtspOutgoingConnection.Register(self)

  /*override def receive: Receive = { // TODO: let connections time out and then open a new one
    case request: RtspRequest =>
      val tcpConnection: ActorRef = _
      val connection = RtspOutgoingConnection.actor(tcpConnection)



  }*/

  override def receive: Receive = {
    case request: RtspRequest =>
      val listener = sender()
      outgoingConnection ! Write(request.copy(requestHeaders =  RtspHeaderField.CSeq(0) +: request.requestHeaders)(request.version))

      context become {
        case Ack =>
          val responseTimeoutTimer = context.scheduleOnce(5 seconds, ResponseTimeout)
          unstashAll()
          context become {
            case Received(response) =>
              responseTimeoutTimer.cancel()
              listener ! response
              unstashAll()
              context.unbecome()

            case ResponseTimeout =>
              listener ! ResponseTimeout
              unstashAll()
              context.unbecome()

            case _: ConnectionClosed =>
              responseTimeoutTimer.cancel()
              stash()
              unstashAll()
              context.unbecome()

            case _ => stash()
          }

        case CommandFailed(write: Write) =>
          listener ! CommandFailed(write)
          unstashAll()
          context.unbecome()

        case _: ConnectionClosed =>
          stash()
          unstashAll()
          context.unbecome()

        case _ => stash()
      }

    case closed: ConnectionClosed =>
      println("Connection closed: " + closed)
      context stop self
  }
}

object RtspSessionActor {
  private[rtsp] def props(tcpConnection: ActorRef, remoteAddress: InetSocketAddress): Props =
    Props(new RtspSessionActor(tcpConnection, remoteAddress))

  private[rtsp] def actor(tcpConnection: ActorRef, remoteAddress: InetSocketAddress)(implicit actorRefFactory: ActorRefFactory): ActorRef =
    actorRefFactory.actorOf(props(tcpConnection, remoteAddress), "RTSP-Actor")

  private case object KeepAlive extends Event

}
