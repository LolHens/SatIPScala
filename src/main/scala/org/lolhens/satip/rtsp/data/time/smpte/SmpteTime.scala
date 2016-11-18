package org.lolhens.satip.rtsp.data.time.smpte

import org.lolhens.satip.util.RichString2._

/**
  * Created by pierr on 15.11.2016.
  */
case class SmpteTime(hours: Int, minutes: Int, seconds: Int, frames: Int, subframes: Int) {
  override def toString: String = List(
    hours.toString.padRight(2, "0"),
    hours.toString.padRight(2, "0"),
    hours.toString.padRight(2, "0"), {
      val framesString = frames match {
        case 0 => None
        case frames => Some(frames.toString.padRight(2, "0"))
      }

      val subframesString = subframes match {
        case 0 => None
        case subframes => Some(subframes.toString.padRight(2, "0"))
      }

      framesString.orElse(subframesString.map(_ => "00"))
        .map(framesString =>
          s"$framesString${
            subframesString.map(subframesString => s".$subframesString").getOrElse("")
          }").getOrElse("")
    }
  ).mkString(":")
}
