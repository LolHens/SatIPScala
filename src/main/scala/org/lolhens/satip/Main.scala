package org.lolhens.satip

import ch.qos.logback.classic.{Level, Logger}
import org.fourthline.cling.model.message.header.STAllHeader
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.model.types.DeviceType
import org.fourthline.cling.registry.{DefaultRegistryListener, Registry, RegistryListener}
import org.fourthline.cling.{UpnpService, UpnpServiceImpl}
import org.seamless.util.logging.LoggingUtil
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler

/**
  * Created by pierr on 22.10.2016.
  */
object Main {
  def main(args: Array[String]): Unit = {
    /*val builder = new ByteStringBuilder()
    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    builder.putInt(16734)
    println(builder.result().toInt)*/
    LoggingUtil.resetRootHandler(new SLF4JBridgeHandler())

    val rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger]
    rootLogger.setLevel(Level.INFO)
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
          println(device.findDevices(new DeviceType("ses-com", "SatIPServer", 1)).mkString(" | "))
          //println("Service discovered " + device + " - " + device.getType + " - " + device.getServices.mkString(", "))
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
