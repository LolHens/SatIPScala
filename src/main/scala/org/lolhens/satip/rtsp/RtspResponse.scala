package org.lolhens.satip.rtsp

import akka.util.ByteString
import fastparse.all._
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.util.ParserUtils._

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspResponse(statusCode: RtspStatusCode,
                        reason: String,
                        headers: List[RtspHeaderField.ResponseField#Value],
                        entity: Option[RtspEntity],
                        version: RtspVersion)

object RtspResponse {
  private val responseParser =
    (Start ~ "RTSP/" ~ RtspVersion.parser ~ s1 ~ RtspStatusCode.parser ~ s1 ~
      (!("." | newline) ~ AnyChar).rep(min = 1).?.! ~ newline ~
      ((!":" ~ AnyChar).rep.! ~ ":" ~ (!newline ~ AnyChar).rep.!.map(_.trim) ~ newline).rep ~ newline ~
      AnyChar.rep.! ~ End).map {
      case (version, statusCode, reason, headerStrings, body) =>
        val headers = headerStrings.toList.flatMap(header => {
          println(RtspHeaderField.valuesMap.get(header._1));
          RtspHeaderField.valuesMap.get(header._1).map(_.Value.fromString(header._2))
        }
        )

        val responseHeaders = headers.collect {
          case responseHeader: RtspHeaderField.ResponseField#Value => responseHeader
        }

        val entity = Some(RtspEntity(
          headers.collect {
            case entityHeader: RtspHeaderField.EntityField#Value =>
              println(entityHeader.getClass)
              entityHeader
          },
          body
        )).filterNot(_.isEmpty)

        RtspResponse(statusCode, reason, responseHeaders, entity, version)
    }

  def fromByteString(byteString: ByteString): RtspResponse = {
    val responseString = byteString.utf8String
    //println(responseString)
    responseParser.parse(responseString).tried.get
  }
}
