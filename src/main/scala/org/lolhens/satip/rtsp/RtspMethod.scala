package org.lolhens.satip.rtsp

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspMethod(name: String) {
  override def toString: String = name

  /*lazy val supportedHeaderFields: List[RtspHeaderField] =
    RtspHeaderField.values.filter(_.supportedMethods.contains(this))*/
}

object RtspMethod {
  val Options = RtspMethod("OPTIONS")
  val Describe = RtspMethod("DESCRIBE")
  val Setup = RtspMethod("SETUP")
  val Play = RtspMethod("PLAY")
  val Pause = RtspMethod("PAUSE")
  val Record = RtspMethod("RECORD")
  val Announce = RtspMethod("ANNOUNCE")
  val Teardown = RtspMethod("TEARDOWN")
  val GetParameter = RtspMethod("GET_PARAMETER")
  val SetParameter = RtspMethod("SET_PARAMETER")
  val Redirect = RtspMethod("REDIRECT")

  lazy val values = List(
    Options,
    Describe,
    Setup,
    Play,
    Pause,
    Record,
    Announce,
    Teardown,
    GetParameter,
    SetParameter,
    Redirect
  )

  lazy val valuesMap: Map[String, RtspMethod] = values.map(e => e.name -> e).toMap
}
