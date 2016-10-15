package org.connect

import akka.util.ByteString
import cats.data.Xor
import monix.eval.Task
import monix.reactive.{Consumer, Observable}
import org.connect.Interface.ConnectedInterface

import scala.concurrent.Future
import scala.util.Failure

/**
  * Created by pierr on 06.10.2016.
  */
abstract class Interface {
  def connect: Future[Xor[Exception, ConnectedInterface]]
}

object Interface {

  abstract class ConnectedInterface {
    def send(byteString: ByteString): Future[Xor[Exception, Unit]]

    def receive: Observable[ByteString]
  }

}