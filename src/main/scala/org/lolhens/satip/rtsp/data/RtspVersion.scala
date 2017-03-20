package org.lolhens.satip.rtsp.data

import fastparse.all._
import org.lolhens.satip.util.ParserUtils._

/**
  * Created by pierr on 13.11.2016.
  */
case class RtspVersion(majorVersion: Int,
                       minorVersion: Int) {
  override def toString: String = s"$majorVersion.$minorVersion"
}

object RtspVersion {
  val parser: Parser[RtspVersion] = (digits.!.map(Integer.parseInt) ~ "." ~ digits.!.map(Integer.parseInt)).map {
    case (majorVersion, minorVersion) =>
      RtspVersion(majorVersion, minorVersion)
  }

  object `1.0` extends RtspVersion(1, 0)

  object `2.0` extends RtspVersion(2, 0)

}
