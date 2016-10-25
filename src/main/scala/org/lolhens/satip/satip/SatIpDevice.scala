package org.lolhens.satip.satip

import org.fourthline.cling.model.meta.RemoteDevice
import org.lolhens.satip.rtsp.{RtspClient, RtspMethod, RtspRequest, RtspStatusCode}
import org.lolhens.satip.satip.SatIpDevice.Tuner

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import fastparse.all._
import org.lolhens.satip.util.ParserUtils._

/**
  * Created by pierr on 23.10.2016.
  */
class SatIpDevice(val baseUrl: String,
                  val friendlyName: String,
                  val uniqueDeviceName: String,
                  val description: String,
                  val capabilities: String,
                  val hasSatteliteBroadcastSupport: Boolean,
                  val hasCableBroadcastSupport: Boolean,
                  val hasTerrestrialBroadcastSupport: Boolean) {
  val responseBodyParser = P(("s=SatIPServer:1" ~ s1 ~ (!space ~ AnyChar).rep(min = 1).! ~ s1).?)

  def checkCapabilities(capabilities: String): Unit = {
    val tunerCount: Map[Tuner, Int] =
      capabilities
        .split(",")
        .map(_.split("-").head.trim.toLowerCase)
        .flatMap(Tuner.valueMap.get)
        .groupBy(e => e)
        .map(e => (e._1, e._2.length)) match {
        case Map.empty =>
          val request = RtspRequest(RtspMethod.describe, s"rtsp://$baseUrl/", 1, 0, Map(
            "Accept" -> "application/sdp",
            "Connection" -> "close"
          ))

          val client = new RtspClient(baseUrl)
          val responseFuture = client.request(request)

          Await.result(responseFuture.map {response =>
            if (response.statusCode == RtspStatusCode.ok) {
              val frontEndInfo = responseBodyParser.parse(response.body).tried.toOption.flatten
              frontEndInfo.map {frontEndInfo =>
                val frontEndCounts = frontEndInfo.split(",")

              }
            }
          }, Duration.Inf)
          ()
      }


  }
}

object SatIpDevice {
  def fromUPnPDevice(device: RemoteDevice) = {
    device.getDetails.getBaseURL
  }

  case class Tuner(name: String)

  object Tuner {
    val values = List(dvbs, dvbs2, dvbt, dvbt2, dvbc, dvbc2)

    lazy val valueMap = values.map(e => (e.name, e)).toMap

    val dvbs = Tuner("dvbs")
    val dvbs2 = Tuner("dvbs2")
    val dvbt = Tuner("dvbt")
    val dvbt2 = Tuner("dvbt2")
    val dvbc = Tuner("dvbc")
    val dvbc2 = Tuner("dvbc2")
  }
}