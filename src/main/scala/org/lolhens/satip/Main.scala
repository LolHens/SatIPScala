package org.lolhens.satip

import org.fourthline.cling.model.message.header.STAllHeader
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.registry.{DefaultRegistryListener, Registry, RegistryListener}
import org.fourthline.cling.{UpnpService, UpnpServiceImpl}

/**
  * Created by pierr on 22.10.2016.
  */
object Main {
  def main(args: Array[String]): Unit = {
    /*val builder = new ByteStringBuilder()
    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    builder.putInt(16734)
    println(builder.result().toInt)*/

    testUpnp()
  }

  def testUpnp() = {
    val clientThread = new Thread(new BinaryLightClient())
    clientThread.setDaemon(false)
    clientThread.start()
  }

  class BinaryLightClient extends Runnable {
    def run(): Unit = {
      try {

        val upnpService: UpnpService = new UpnpServiceImpl()

        // Add a listener for device registration events
        upnpService.getRegistry.addListener(
          createRegistryListener(upnpService)
        )

        // Broadcast a search message for all devices
        upnpService.getControlPoint.search(
          new STAllHeader()
        )

      } catch {
        case e: Exception =>
          System.err.println("Exception occured: " + e)
          System.exit(1)
      }
    }

    def createRegistryListener(upnpService: UpnpService): RegistryListener = {
      return new DefaultRegistryListener() {

        //ServiceId serviceId = new UDAServiceId("SwitchPower")

        override def remoteDeviceAdded(registry: Registry, device: RemoteDevice): Unit = {
          println("Service discovered " + device + " - " + device)
          /*Service switchPower;
          if ((switchPower = device.findService(serviceId)) != null) {
            System.out.println("Service discovered: " + switchPower);
            executeAction(upnpService, switchPower);
          }*/
        }

        override def remoteDeviceRemoved(registry: Registry, device: RemoteDevice): Unit = {
          /*Service switchPower;
          if ((switchPower = device.findService(serviceId)) != null) {
            System.out.println("Service disappeared: " + switchPower);
          }*/
          println("Service disappeared" + device)
        }

      }
    }
  }

}
