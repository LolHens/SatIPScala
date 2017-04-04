package org.lolhens.satip.rtsp

import akka.actor.{Actor, ActorRef, ActorRefFactory, Deploy, Props, Stash}
import akka.io.{IO, Tcp}
import org.lolhens.satip.rtsp.Rtsp.{CommandFailed, Connect, Connected}

import scala.language.postfixOps

/**
  * Created by pierr on 27.03.2017.
  */
private[rtsp] class RtspManager extends Actor with Stash {
  override def receive: Receive = {
    case connect@Connect(socketAddress, multicast) =>
      val listener = sender()

      IO(Tcp)(context.system) ! Tcp.Connect(socketAddress)

      context become {
        case Tcp.Connected(remoteAddress, localAddress) =>
          val tcpConnection = sender()

          val connection = RtspConnection.actor(tcpConnection, remoteAddress)

          listener tell(Connected(remoteAddress, localAddress), connection)

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

object RtspManager {
  private[rtsp] val props: Props = Props[RtspManager]

  private[rtsp] def actor(implicit actorRefFactory: ActorRefFactory): ActorRef =
    actorRefFactory.actorOf(props.withDeploy(Deploy.local), "IO-RTSP")
}
