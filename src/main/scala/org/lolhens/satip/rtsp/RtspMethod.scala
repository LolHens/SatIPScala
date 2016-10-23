package org.lolhens.satip.rtsp

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspMethod(name: String) extends AnyVal

object RtspMethod {
  def values = List(
    describe,
    announce,
    getParameter,
    options,
    pause,
    play,
    record,
    redirect,
    setup,
    setParameter,
    teardown
  )

  lazy val valuesMap: Map[String, RtspMethod] = values.map(e => (e.name, e)).toMap

  val describe = RtspMethod("DESCRIBE")
  val announce = RtspMethod("ANNOUNCE")
  val getParameter = RtspMethod("GET_PARAMETER")
  val options = RtspMethod("OPTIONS")
  val pause = RtspMethod("PAUSE")
  val play = RtspMethod("PLAY")
  val record = RtspMethod("RECORD")
  val redirect = RtspMethod("REDIRECT")
  val setup = RtspMethod("SETUP")
  val setParameter = RtspMethod("SET_PARAMETER")
  val teardown = RtspMethod("TEARDOWN")
}
