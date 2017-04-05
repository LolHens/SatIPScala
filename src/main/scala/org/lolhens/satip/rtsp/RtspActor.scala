package org.lolhens.satip.rtsp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props, Stash}
import akka.pattern.ask
import akka.util.Timeout
import org.lolhens.satip.rtsp.Rtsp._
import org.lolhens.satip.rtsp.RtspManager._
import org.lolhens.satip.rtsp.RtspActor.KeepAlive
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.util.ContextScheduler

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by pierr on 03.04.2017.
  */
private[rtsp] class RtspActor(tcpConnection: ActorRef, remoteAddress: InetSocketAddress) extends Actor with Stash with ContextScheduler {
  val outgoingConnection: ActorRef = RtspOutgoingConnection.actor(tcpConnection)

  outgoingConnection ! RtspOutgoingConnection.Register(self)

  context.schedule(0 seconds, 5 seconds, KeepAlive)

  override def receive: Receive = {
    case request: RtspRequest =>
      val listener = sender()
      outgoingConnection ! Write(request)

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

    case KeepAlive =>
      implicit val rtspVersion = RtspVersion(1, 0)
      val options = RtspRequest.options(s"rtsp://${"10.1.2.6"}:554/" /*stream=0"*/ , cSeq = 1, List(
        RtspHeaderField.Accept("application/sdp") //,
        //RtspHeaderField.Session("0")
      ))

      println("keep alive")

      implicit val timeout = Timeout(1 second)
      import context.dispatcher
      self ? options

    case closed: ConnectionClosed =>
      println("Connection closed: " + closed)
      context stop self
  }
}

object RtspActor {
  private[rtsp] def props(tcpConnection: ActorRef, remoteAddress: InetSocketAddress): Props =
    Props(new RtspActor(tcpConnection, remoteAddress))

  private[rtsp] def actor(tcpConnection: ActorRef, remoteAddress: InetSocketAddress)(implicit actorRefFactory: ActorRefFactory): ActorRef =
    actorRefFactory.actorOf(props(tcpConnection, remoteAddress), "RTSP-Actor")

  private case object KeepAlive extends Event

}
