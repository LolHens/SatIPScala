package org.lolhens.satip.rtsp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}

/**
  * Created by pierr on 27.03.2017.
  */
class RtspActor(socketAddress: InetSocketAddress) extends Actor {
  override def receive: Receive = {
    case _ => ???
  }
}

object RtspActor {
  def props(socketAddress: InetSocketAddress) = Props(classOf[RtspActor], socketAddress)

  def actor(socketAddress: InetSocketAddress)(implicit actorSystem: ActorRefFactory): ActorRef =
    actorSystem.actorOf(props(socketAddress))
}
