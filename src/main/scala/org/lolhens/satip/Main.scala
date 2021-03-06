package org.lolhens.satip

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import org.jupnp.model.message.header.STAllHeader
import org.jupnp.model.meta.RemoteDevice
import org.jupnp.model.types.DeviceType
import org.jupnp.registry.{DefaultRegistryListener, Registry, RegistryListener}
import org.jupnp.{UpnpService, UpnpServiceImpl}
import org.lolhens.satip.rtsp.Rtsp.{Connect, Connected}
import org.lolhens.satip.rtsp._
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.satip.SatIpDiscoveryActor
import org.lolhens.satip.upnp.UpnpServiceActor
import org.lolhens.satip.upnp.UpnpServiceActor.DeviceUpdated

import scala.xml.XML

/**
  * Created by pierr on 22.10.2016.
  */
object Main {
  def main(args: Array[String]): Unit = {
    /*val builder = new ByteStringBuilder()
    implicit val byteOrder = ByteOrder.BIG_ENDIAN
    builder.putInt(16734)
    println(builder.result().toInt)*/
    //LoggingUtil.resetRootHandler(new SLF4JBridgeHandler())

    //val rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger]
    //rootLogger.setLevel(Level.INFO)
    //testUpnp()
    //RtspSession.test
    testUpnp3()
    //testRtsp()
  }

  def testRtsp() = {
    val actorSystem = ActorSystem()

    class RtspListener extends Actor {
      IO(Rtsp)(actorSystem) ! Connect(new InetSocketAddress("10.1.15.237", 554))

      override def receive: Receive = {
        case Connected(remote, _) =>
          println("Connected")
          val connection = sender()

          //connection ! Register(self)

          implicit val rtspVersion = RtspVersion(1, 0)
          val request = RtspRequest.describe(s"rtsp://${"10.1.15.237"}:554/" /*stream=0"*/ , List(
            //RtspHeaderField.Accept("application/sdp")//,
            //RtspHeaderField.Session("0")
          ), RtspEntity(Nil, ""))

          val request2 = RtspRequest.options(s"rtsp://${"10.1.15.237"}:554/" /*stream=0"*/ , List(
            RtspHeaderField.Accept("application/sdp") //,
            //RtspHeaderField.Session("0")
          ))

          connection ! request2

          val request3 = RtspRequest.setup(s"rtsp://${"10.1.15.237"}:554/")

          connection ! request3

          context.become {
            case msg => println(msg)
          }

        case msg => println(msg)
      }
    }

    val props = Props(new RtspListener())
    actorSystem.actorOf(props)
  }

  def testUpnp3() = {
    implicit val actorSystem = ActorSystem()

    val satIpDiscoveryActor = SatIpDiscoveryActor.actor()

  }

  def testUpnp2() = {
    class UpnpListener extends Actor {
      val upnpService = context.actorOf(UpnpServiceActor.props)
      upnpService ! UpnpServiceActor.Register(self)

      override def receive: Receive = {
        case DeviceUpdated(_, _) =>
        case e => println(e)
      }
    }

    val props = Props[UpnpListener]

    val actorSystem = ActorSystem()
    actorSystem.actorOf(props)
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
          val devices = device.findDevices(new DeviceType("ses-com", "SatIPServer", 1))
          println(devices.map(device => (XML.load(device.getIdentity.getDescriptorURL) \ "device").map(_.namespace).mkString(",")).mkString("\n---\n"))
          devices.map { device =>
            val ip = device.getIdentity.getDescriptorURL.getHost
            println(ip)

            println(device.getDetails.getBaseURL)
            println(device.getDetails.getFriendlyName)
            //println(device.getDetails.get)
            println(device.getIdentity.getUdn)
            println(device)
            println("test")
          }
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
