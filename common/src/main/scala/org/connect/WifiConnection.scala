package org.connect

import java.net.{NetworkInterface, Socket}
import scala.collection.JavaConversions._

/**
  * Created by pierr on 08.10.2016.
  */
class WifiConnection {
  val socket = new Socket("localhost", 80)
  socket.bind("")

}
