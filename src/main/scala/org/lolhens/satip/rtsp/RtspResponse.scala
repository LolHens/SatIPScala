package org.lolhens.satip.rtsp

import java.nio.ByteOrder

import akka.util.ByteString
import fastparse.all._
import org.lolhens.satip.rtsp.data.RtspVersion
import org.lolhens.satip.util.ParserUtils._

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspResponse(rtspVersion: RtspVersion,
                        statusCode: RtspStatusCode,
                        reason: String,
                        headers: List[RtspHeaderField.ResponseField#Value],
                        entity: Option[RtspEntity])

object RtspResponse {
  private val responseParser =
    (Start ~ "RTSP/" ~ RtspVersion.parser ~ s1 ~ RtspStatusCode.parser ~ s1 ~
      (!("." | newline) ~ AnyChar).rep(min = 1).?.! ~ newline ~
      ((!":" ~ AnyChar).rep.! ~ ":" ~ (!newline ~ AnyChar).rep.!.map(_.trim) ~ newline).rep ~ newline ~
      AnyChar.rep.! ~ End).map {
      case (version, statusCode, reason, headers, body) =>
        RtspResponse(version, statusCode, reason,
          headers
            .map(header => RtspHeaderField.valuesMap.get(header._1) -> header._2)
            .collect {
              case (Some(responseHeader: RtspHeaderField.ResponseField), value: String) => responseHeader.Value.fromString(value)
            }
            .toList,
          Some(RtspEntity(headers
            .map(header => RtspHeaderField.valuesMap.get(header._1) -> header._2)
            .collect {
              case (Some(entityHeader: RtspHeaderField.EntityField), value: String) => entityHeader.Value.fromString(value)
            }
            .toList, body)).filterNot(_.isEmpty)
        )
    }

  def fromByteString(byteString: ByteString): RtspResponse = {
    val responseString = byteString.utf8String
    //println(responseString)
    responseParser.parse(responseString).tried.get
  }
}
