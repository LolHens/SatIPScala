package org.lolhens.satip.rtsp

import org.lolhens.satip.rtsp.RtspMethod._

/**
  * Created by pierr on 13.11.2016.
  */
case class RtspHeaderField(name: String, methods: List[RtspMethod]) {
  def apply(value: String): (this.type, String) = this -> value
}

object RtspHeaderField {

  trait RequestField

  trait ResponseField

  trait GeneralField extends RequestField with ResponseField

  trait EntityField

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
  val ContentEncoding = new RtspHeaderField("Content-Encoding", List(SetParameter)) with EntityField
  val ContentEncoding = new RtspHeaderField("Content-Encoding", List(Describe, Announce)) with EntityField
  val ContentLanguage = new RtspHeaderField("Content-Language", List(Describe, Announce)) with EntityField
  val ContentLength = new RtspHeaderField("Content-Length", List(SetParameter, Announce)) with EntityField
  val ContentLength = new RtspHeaderField("Content-Length", entity) with EntityField
  val ContentLocation = new RtspHeaderField("Content-Location", entity) with EntityField
  val ContentType = new RtspHeaderField("Content-Type", List(SetParameter, Announce)) with EntityField
  val ContentType = new RtspHeaderField("Content-Type", entity) with ResponseField
  val CSeq = new RtspHeaderField("CSeq", all) with GeneralField
  val Date = new RtspHeaderField("Date", all) with GeneralField
  val Expires = new RtspHeaderField("Expires", List(Describe, Announce)) with EntityField
  val From = new RtspHeaderField("From", all) with RequestField
  val IfModifiedSince = new RtspHeaderField("If-Modified-Since", List(Describe, Setup)) with RequestField
  val LastModified = new RtspHeaderField("Last-Modified", entity) with EntityField
  // val ProxyAuthenticate = new RtspHeaderField("Proxy-Authenticate")
  val ProxyRequire = new RtspHeaderField("Proxy-Require", all) with RequestField
  val Public = new RtspHeaderField("Public", all) with ResponseField
  val Range = new RtspHeaderField("Range", List(Play, Pause, Record)) with RequestField
  val Range = new RtspHeaderField("Range", List(Play, Pause, Record)) with ResponseField
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

  /*Accept               R      opt.      entity
  Accept-Encoding      R      opt.      entity
  Accept-Language      R      opt.      all
  Allow                r      opt.      all
  Authorization        R      opt.      all
  Bandwidth            R      opt.      all
  Blocksize            R      opt.      all but OPTIONS, TEARDOWN
  Cache-Control        g      opt.      SETUP
  Conference           R      opt.      SETUP
  Connection           g      req.      all
  Content-Base         e      opt.      entity
  Content-Encoding     e      req.      SET_PARAMETER
  Content-Encoding     e      req.      DESCRIBE, ANNOUNCE
  Content-Language     e      req.      DESCRIBE, ANNOUNCE
  Content-Length       e      req.      SET_PARAMETER, ANNOUNCE
  Content-Length       e      req.      entity
  Content-Location     e      opt.      entity
  Content-Type         e      req.      SET_PARAMETER, ANNOUNCE
  Content-Type         r      req.      entity
  CSeq                 g      req.      all
  Date                 g      opt.      all
  Expires              e      opt.      DESCRIBE, ANNOUNCE
  From                 R      opt.      all
  If-Modified-Since    R      opt.      DESCRIBE, SETUP
  Last-Modified        e      opt.      entity
  Proxy-Authenticate
  Proxy-Require        R      req.      all
  Public               r      opt.      all
  Range                R      opt.      PLAY, PAUSE, RECORD
  Range                r      opt.      PLAY, PAUSE, RECORD
  Referer              R      opt.      all
  Require              R      req.      all
  Retry-After          r      opt.      all
  RTP-Info             r      req.      PLAY
  Scale                Rr     opt.      PLAY, RECORD
  Session              Rr     req.      all but SETUP, OPTIONS
  Server               r      opt.      all
  Speed                Rr     opt.      PLAY
  Transport            Rr     req.      SETUP
  Unsupported          r      req.      all
  User-Agent           R      opt.      all
  Via                  g      opt.      all
  WWW-Authenticate     r      opt.      all*/
}
