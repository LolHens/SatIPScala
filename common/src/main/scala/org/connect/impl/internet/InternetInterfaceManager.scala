package org.connect.impl.internet

import java.net.NetworkInterface

import org.connect.{Interface, InterfaceManager}
import scala.collection.JavaConversions._

/**
  * Created by pierr on 08.10.2016.
  */
class InternetInterfaceManager extends InterfaceManager {
  override def interfaces: List[Interface] = fetchInterfaces // TODO: Cache interfaces

  def fetchInterfaces: List[Interface] = {
    NetworkInterface.getNetworkInterfaces.map(interface => new InternetInterface(interface)).toList
  }
}
