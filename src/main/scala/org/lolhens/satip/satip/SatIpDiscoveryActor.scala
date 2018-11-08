package org.lolhens.satip.satip

import java.util.regex.Pattern

import akka.actor.{Actor, ActorRef, ActorRefFactory, Props}
import akka.http.scaladsl.model.Uri
import org.lolhens.satip.rtsp._
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.satip.SatIpDiscoveryActor.CapabilitiesResolved
import org.lolhens.satip.upnp.UpnpServiceActor
import org.lolhens.satip.upnp.device.{DeviceType, UpnpDevice}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by u016595 on 21.03.2017.
  */
class SatIpDiscoveryActor(upnpService: ActorRef) extends Actor {
  upnpService ! UpnpServiceActor.Register(self)

  override def receive: Receive = {
    case UpnpServiceActor.DeviceAdded(_, device) =>
      device.deviceType match {
        case DeviceType("ses-com", "SatIPServer", version) =>
          device.remoteDetails match {
            case Some(remoteDetails) =>
              val capabilities: List[Tuner] = (remoteDetails.descriptor \ "device" \ "X_SATIPCAP").headOption match {
                case Some(node) if node.prefix == "satip" && node.namespace == "urn:ses-com:satip" =>
                  node.text.split(Pattern.quote(",")).toList
                    .map(_.split(Pattern.quote("-")).head.trim.toLowerCase)
                    .flatMap(Tuner.valueMap.get)

                case None =>
                  Nil
              }

              capabilities match {
                case Nil =>


                case capabilities =>
                  self ! CapabilitiesResolved(device, capabilities)
              }

              val caps2 = {
                implicit val rtspVersion = RtspVersion(1, 0)
                val baseurl = "http://10.1.2.5"

                val request = RtspRequest.describe(Uri(s"rtsp://${remoteDetails.remoteHost}:554/stream=0"), List(
                  RtspHeaderField.Accept("application/sdp"),
                  RtspHeaderField.Connection("close")
                ), RtspEntity(Nil, ""))

                val client = new RtspClient(remoteDetails.remoteHost)
                val responseFuture = client.request(request)

                import fastparse.all._
                import org.lolhens.satip.util.ParserUtils._

                val responseBodyParser: Parser[Option[String]] =
                  ("s=SatIPServer:1" ~ s1 ~ (!space ~ AnyChar).rep(min = 1).! ~ s1).?

                Await.result(responseFuture.map { response =>
                  println(request.request)
                  println(response)
                  response.statusCode match {
                    case RtspStatusCode.Ok =>
                      val frontEndInfo =
                        response.entity.map(_.body)
                          .flatMap(body => responseBodyParser.parse(body).tried.toOption.flatten)
                      frontEndInfo.map {
                        frontEndInfo =>
                          val frontEndCounts = frontEndInfo.split(",")
                          val tunerCounts2 =
                            List("dvbs2", "dvbt", "dvbc")
                              .zip(
                                frontEndCounts
                                  .take(3)
                                  .map(((_: String).trim) andThen Integer.parseInt)
                                  .padTo(3, 0)
                              )

                          ???
                      }

                    case RtspStatusCode.NotFound =>
                    // the Sat>Ip server has no active stream

                    case _ =>
                    //???
                  }
                }, Duration.Inf)
              }

              println("---------")
              println(caps2)

              println(version)
              println(remoteDetails.descriptorUrl)
              println(capabilities)
              println("-")
              println((remoteDetails.descriptor \ "device" \ "X_SATIPCAP").mkString(","))
              println((remoteDetails.descriptor \ "device" \ "X_SATIPCAP").filter(node => node.prefix == "satip" && node.namespace == "urn:ses-com:satip").map(_.toString()).mkString(","))
              println((remoteDetails.descriptor \ "device" \ "satip:X_SATIPCAP").map(_.getNamespace("satip")).mkString(","))
              println((remoteDetails.descriptor \ "device").map(_.namespace).mkString(","))
              val ip = remoteDetails.remoteInterfaceAddress
              println(ip)

              //println(device.getDetails.getBaseURL)
              println(device.friendlyName)
              //println(device.getDetails.get)
              //println(device.getIdentity.getUdn)
              println(device)
            //println("test")

            case None =>
          }

        case _ =>
      }
  }
}

object SatIpDiscoveryActor {
  def props(upnpService: ActorRef) = Props(classOf[SatIpDiscoveryActor], upnpService)

  def actor(upnpService: ActorRef = null)(implicit actorSystem: ActorRefFactory): ActorRef =
    actorSystem.actorOf(props(Option(upnpService).getOrElse(UpnpServiceActor.actor)))

  case class CapabilitiesResolved(upnpDevice: UpnpDevice, capabilities: List[Tuner])

  //private case class SatIpDeviceAdded()
}


