package org.lolhens.satip.rtsp

import java.nio.ByteOrder

import akka.util.ByteString
import fastparse.all._
import org.lolhens.satip.util.ParserUtils._

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspResponse(majorVersion: Int = 1,
                        minorVersion: Int,
                        statusCode: RtspStatusCode,
                        reasonPhrase: String,
                        headers: Map[String, String],
                        body: String)

object RtspResponse {
  private val responseParser =
    P((Start ~ "RTSP/" ~
      digits.!.map(Integer.parseInt) ~ "." ~ digits.!.map(Integer.parseInt) ~ s1 ~
      digits.!.map((Integer.parseInt(_)) andThen (RtspStatusCode(_))) ~ s1 ~
      (!"." ~ AnyChar).rep(min = 1).?.! ~ "\r\n" ~
      ((!":" ~ AnyChar).rep.! ~ ":" ~ (!"\r\n" ~ AnyChar).rep.!.map(_.trim)).rep(sep = !"\r\n\r\n" ~ "\r\n").map(_.toMap) ~ "\r\n\r\n" ~
      AnyChar.rep.! ~ End)
      .map {
        case (majorVersion, minorVersion, statusCode, reasonPhrase, headers, body) =>
          RtspResponse(majorVersion, minorVersion, statusCode, reasonPhrase, headers, body)
      })

  def fromByteString(byteString: ByteString)(implicit byteOrder: ByteOrder): RtspResponse = {
    val responseString = byteString.utf8String
    responseParser.parse(responseString).tried.get
  }
}
