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
                        reasonPhrase: String,
                        responseHeaders: List[RtspHeaderField.ResponseField#Value],
                        entity: Option[RtspEntity])

object RtspResponse {
  private val responseParser =
    P((Start ~ "RTSP/" ~
      digits.!.map(Integer.parseInt) ~ "." ~ digits.!.map(Integer.parseInt) ~ s1 ~
      digits.!.map((Integer.parseInt(_: String)) andThen (RtspStatusCode(_))) ~ s1 ~
      (!("." | "\r\n") ~ AnyChar).rep(min = 1).?.! ~ "\r\n" ~
      ((!":" ~ AnyChar).rep.! ~ ":" ~ (!"\r\n" ~ AnyChar).rep.!.map(_.trim) ~ "\r\n").rep ~ "\r\n" ~
      AnyChar.rep.! ~ End)
      .map {
        case (majorVersion, minorVersion, statusCode, reasonPhrase, headers, body) =>
          RtspResponse(
            RtspVersion(majorVersion, minorVersion), statusCode, reasonPhrase,
            headers
              .map(header => RtspHeaderField.valuesMap.get(header._1) -> header._2)
              .collect {
                case (Some(responseHeader: RtspHeaderField.ResponseField), value: String) => responseHeader(value)
              }
              .toList,
            Some(RtspEntity(headers
              .map(header => RtspHeaderField.valuesMap.get(header._1) -> header._2)
              .collect {
                case (Some(entityHeader: RtspHeaderField.EntityField), value: String) => entityHeader.apply(value)
              }
              .toList, body)).filterNot(_.isEmpty)
          )
      })

  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtspResponse = {
    val responseString = byteString.utf8String
    println(responseString)
    responseParser.parse(responseString).tried.get
  }
}
