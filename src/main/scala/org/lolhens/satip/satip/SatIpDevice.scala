package org.lolhens.satip.satip

import fastparse.all._
import org.fourthline.cling.model.meta.RemoteDevice
import org.lolhens.satip.rtsp._
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.util.ParserUtils._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by pierr on 23.10.2016.
  */
class SatIpDevice(val baseUrl: String,
                  val friendlyName: String,
                  val uniqueDeviceName: String,
                  val description: String,
                  val capabilities: List[Tuner],
                  val hasSatteliteBroadcastSupport: Boolean,
                  val hasCableBroadcastSupport: Boolean,
                  val hasTerrestrialBroadcastSupport: Boolean /*,
                  upnpDevice: UpnpDevice = null*/) {
  val responseBodyParser: Parser[Option[String]] =
    ("s=SatIPServer:1" ~ s1 ~ (!space ~ AnyChar).rep(min = 1).! ~ s1).?

  checkCapabilities(Nil)

  def checkCapabilities(capabilities: List[String]): Unit = {
    implicit val rtspVersion = RtspVersion(1, 0)
    val tunerCount: Map[Tuner, Int] =
      capabilities
        .flatMap(Tuner.valueMap.get)
        .groupBy(e => e)
        .map(e => (e._1, e._2.length)) match {
        case empty if empty.isEmpty =>
          val request = RtspRequest.describe(s"rtsp://$baseUrl/", 0, List(
            RtspHeaderField.Accept("application/sdp"),
            RtspHeaderField.Connection("close")
          ), RtspEntity(Nil, ""))

          val client = new RtspClient(baseUrl)
          val responseFuture = client.request(request)

          Await.result(responseFuture.map { response =>
            println(response)
            response.statusCode match {
              case RtspStatusCode.ok =>
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

              case RtspStatusCode.notFound =>
              // the Sat>Ip server has no active stream

              case _ =>
                ???
            }
          }, Duration.Inf)
          ???

        //case
      }


  }
}

object SatIpDevice {
  def fromUPnPDevice(device: RemoteDevice) = {
    device.getDetails.getBaseURL
  }
}