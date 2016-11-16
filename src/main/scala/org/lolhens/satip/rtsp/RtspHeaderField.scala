package org.lolhens.satip.rtsp

import org.lolhens.satip.rtsp.RtspMethod._

/**
  * Created by pierr on 13.11.2016.
  */
case class RtspHeaderField(name: String, methods: List[RtspMethod])

object RtspHeaderField {

  trait RequestField

  trait ResponseField

  private val all = RtspMethod.values
  private val entity = List(Describe, GetParameter)

  val Accept = new RtspHeaderField("Accept", entity) with RequestField
  val AcceptEncoding = new RtspHeaderField("Accept-Encoding", entity)
  val AcceptLanguage = new RtspHeaderField("Accept-Language", all)
  val Allow = new RtspHeaderField("Allow", all)
  val Authorization = new RtspHeaderField("Authorization", all)
  val Bandwidth = new RtspHeaderField("Bandwidth", all)
  val Blocksize = new RtspHeaderField("Blocksize", List(all but OPTIONS, TEARDOWN))
  val CacheControl = new RtspHeaderField("Cache-Control", List(Setup))
  val Conference = new RtspHeaderField("Conference", List(Setup))
  val Connection = new RtspHeaderField("Connection", all)
  val ContentBase = new RtspHeaderField("Content-Base", entity)
  val ContentEncoding = new RtspHeaderField("Content-Encoding", List(SetParameter))
  val ContentEncoding = new RtspHeaderField("Content-Encoding", List(Describe, Announce))
  val ContentLanguage = new RtspHeaderField("Content-Language", List(Describe, Announce))
  val ContentLength = new RtspHeaderField("Content-Length", List(SetParameter, Announce))
  val ContentLength = new RtspHeaderField("Content-Length", entity)
  val ContentLocation = new RtspHeaderField("Content-Location", entity)
  val ContentType = new RtspHeaderField("Content-Type", List(SetParameter, Announce))
  val ContentType = new RtspHeaderField("Content-Type", entity)
  val CSeq = new RtspHeaderField("CSeq", all)
  val Date = new RtspHeaderField("Date", all)
  val Expires = new RtspHeaderField("Expires", List(Describe, Announce))
  val From = new RtspHeaderField("From", all)
  val IfModifiedSince = new RtspHeaderField("If-Modified-Since", List(Describe, Setup))
  val LastModified = new RtspHeaderField("Last-Modified", entity)
  val ProxyAuthenticate = new RtspHeaderField("Proxy-Authenticate")
  val ProxyRequire = new RtspHeaderField("Proxy-Require", all)
  val Public = new RtspHeaderField("Public", all)
  val Range = new RtspHeaderField("Range", List(Play, Pause, Record))
  val Range = new RtspHeaderField("Range", List(Play, Pause, Record))
  val Referer = new RtspHeaderField("Referer", all)
  val Require = new RtspHeaderField("Require", all)
  val RetryAfter = new RtspHeaderField("Retry-After", all)
  val RTPInfo = new RtspHeaderField("RTP-Info", List(Play))
  val Scale = new RtspHeaderField("Scale", List(Play, Record))
  val Session = new RtspHeaderField("Session", List(all but SETUP, OPTIONS))
  val Server = new RtspHeaderField("Server", all)
  val Speed = new RtspHeaderField("Speed", List(Play))
  val Transport = new RtspHeaderField("Transport", List(Setup))
  val Unsupported = new RtspHeaderField("Unsupported", all)
  val UserAgent = new RtspHeaderField("User-Agent", all)
  val Via = new RtspHeaderField("Via", all)
  val WWWAuthenticate = new RtspHeaderField("WWW-Authenticate", all)

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
