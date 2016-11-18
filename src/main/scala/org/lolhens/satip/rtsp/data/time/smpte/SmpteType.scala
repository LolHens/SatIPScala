package org.lolhens.satip.rtsp.data.time.smpte

/**
  * Created by pierr on 17.11.2016.
  */
case class SmpteType(name: String)

object SmpteType {
  val smpte = SmpteType("smpte")
  val smpte30Drop = SmpteType("smpte-30-drop")
  val smpte25 = SmpteType("smpte-25")

  val values: List[SmpteType] = List(smpte, smpte30Drop, smpte25)

  lazy val valuesMap = values.map(e => e.name -> e).toMap
}