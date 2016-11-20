package org.lolhens.satip.rtsp

import org.lolhens.satip.rtsp.RtspMethod._

/**
  * Created by pierr on 13.11.2016.
  */
case class RtspHeaderField(name: String, methods: List[RtspMethod]) {
  case class Value(value: String) {
    val headerField = RtspHeaderField.this
  }

  def apply(value: String): Value = Value(value)
}

object RtspHeaderField {

  trait RequestField extends RtspHeaderField

  trait ResponseField extends RtspHeaderField

  trait GeneralField extends RequestField with ResponseField

  trait EntityField extends RtspHeaderField

  private val all = RtspMethod.values

  private def except(methods: List[RtspMethod]) = RtspMethod.values.filterNot(methods.contains)

  private val entity = List(Describe, GetParameter)

  val Accept = new RtspHeaderField("Accept", entity) with RequestField
  val AcceptEncoding = new RtspHeaderField("Accept-Encoding", entity) with RequestField
  val AcceptLanguage = new RtspHeaderField("Accept-Language", all) with RequestField
  val Allow = new RtspHeaderField("Allow", all) with ResponseField
  val Authorization = new RtspHeaderField("Authorization", all) with RequestField
  val Bandwidth = new RtspHeaderField("Bandwidth", all) with RequestField
  val Blocksize = new RtspHeaderField("Blocksize", except(List(Options, Teardown))) with RequestField
  val CacheControl = new RtspHeaderField("Cache-Control", List(Setup)) with GeneralField
  val Conference = new RtspHeaderField("Conference", List(Setup)) with RequestField
  val Connection = new RtspHeaderField("Connection", all) with GeneralField
  val ContentBase = new RtspHeaderField("Content-Base", entity) with EntityField
  val ContentEncoding = new RtspHeaderField("Content-Encoding", List(SetParameter, Describe, Announce)) with EntityField
  val ContentLanguage = new RtspHeaderField("Content-Language", List(Describe, Announce)) with EntityField
  val ContentLength = new RtspHeaderField("Content-Length", List(SetParameter, Announce) ++ entity) with EntityField
  val ContentLocation = new RtspHeaderField("Content-Location", entity) with EntityField
  val ContentType = new RtspHeaderField("Content-Type", List(SetParameter, Announce) ++ entity) with EntityField with ResponseField
  val CSeq = new RtspHeaderField("CSeq", all) with GeneralField
  val Date = new RtspHeaderField("Date", all) with GeneralField
  val Expires = new RtspHeaderField("Expires", List(Describe, Announce)) with EntityField
  val From = new RtspHeaderField("From", all) with RequestField
  val IfModifiedSince = new RtspHeaderField("If-Modified-Since", List(Describe, Setup)) with RequestField
  val LastModified = new RtspHeaderField("Last-Modified", entity) with EntityField
  // val ProxyAuthenticate = new RtspHeaderField("Proxy-Authenticate")
  val ProxyRequire = new RtspHeaderField("Proxy-Require", all) with RequestField
  val Public = new RtspHeaderField("Public", all) with ResponseField
  val Range = new RtspHeaderField("Range", List(Play, Pause, Record)) with RequestField with ResponseField
  val Referer = new RtspHeaderField("Referer", all) with RequestField
  val Require = new RtspHeaderField("Require", all) with RequestField
  val RetryAfter = new RtspHeaderField("Retry-After", all) with ResponseField
  val RTPInfo = new RtspHeaderField("RTP-Info", List(Play)) with ResponseField
  val Scale = new RtspHeaderField("Scale", List(Play, Record)) with RequestField with ResponseField
  val Session = new RtspHeaderField("Session", except(List(Setup, Options))) with RequestField with ResponseField
  val Server = new RtspHeaderField("Server", all) with ResponseField
  val Speed = new RtspHeaderField("Speed", List(Play)) with RequestField with ResponseField
  val Transport = new RtspHeaderField("Transport", List(Setup)) with RequestField with ResponseField
  val Unsupported = new RtspHeaderField("Unsupported", all) with ResponseField
  val UserAgent = new RtspHeaderField("User-Agent", all) with RequestField
  val Via = new RtspHeaderField("Via", all) with GeneralField
  val WWWAuthenticate = new RtspHeaderField("WWW-Authenticate", all) with ResponseField

  val values: List[RtspHeaderField] = List(
    Accept,
    AcceptEncoding,
    AcceptLanguage,
    Allow,
    Authorization,
    Bandwidth,
    Blocksize,
    CacheControl,
    Conference,
    Connection,
    ContentBase,
    ContentEncoding,
    ContentLanguage,
    ContentLength,
    ContentLocation,
    ContentType,
    CSeq,
    Date,
    Expires,
    From,
    IfModifiedSince,
    LastModified,
    ProxyRequire,
    Public,
    Range,
    Referer,
    Require,
    RetryAfter,
    RTPInfo,
    Scale,
    Session,
    Server,
    Speed,
    Transport,
    Unsupported,
    UserAgent,
    Via,
    WWWAuthenticate
  )

  lazy val valuesMap: Map[String, RtspHeaderField] = values.map(e => e.name -> e).toMap
}
