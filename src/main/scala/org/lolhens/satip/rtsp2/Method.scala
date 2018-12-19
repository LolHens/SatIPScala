package org.lolhens.satip.rtsp2

/**
  * Created by pierr on 23.10.2016.
  */
case class Method(name: String) {
  override def toString: String = name

  /*lazy val supportedHeaderFields: List[RtspHeaderField] =
    RtspHeaderField.values.filter(_.supportedMethods.contains(this))*/
}

object Method {
  val Options = Method("OPTIONS")
  val Describe = Method("DESCRIBE")
  val Setup = Method("SETUP")
  val Play = Method("PLAY")
  val Pause = Method("PAUSE")
  val Record = Method("RECORD")
  val Announce = Method("ANNOUNCE")
  val Teardown = Method("TEARDOWN")
  val GetParameter = Method("GET_PARAMETER")
  val SetParameter = Method("SET_PARAMETER")
  val Redirect = Method("REDIRECT")

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

  lazy val valuesMap: Map[String, Method] = values.map(e => e.name -> e).toMap
}
