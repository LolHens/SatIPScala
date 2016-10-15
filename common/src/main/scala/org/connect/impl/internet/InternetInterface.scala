package org.connect.impl.internet

import java.net.NetworkInterface

import akka.util.ByteString
import cats.data.Xor
import monix.eval.Task
import monix.reactive.Observable
import org.connect.Interface
import org.connect.Interface.ConnectedInterface

import scala.concurrent.Future

/**
  * Created by pierr on 08.10.2016.
  */
class InternetInterface(networkInterface: NetworkInterface) extends Interface {
  override def connect: Future[Xor[Exception, ConnectedInterface]] = ???
}
