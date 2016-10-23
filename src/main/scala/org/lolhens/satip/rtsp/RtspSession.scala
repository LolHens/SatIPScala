package org.lolhens.satip.rtsp

import java.util.regex.Pattern

/**
  * Created by pierr on 23.10.2016.
  */
class RtspSession(val rtspDevice: RtspDevice,
                  val rtspSessionId: String,
                  val rtspSessionTTL: Int = 0,
                  val rtspStreamId: String,
                  val clientRtpPort: Int,
                  val clientRtcpPort: Int,
                  val serverRtpPort: Int,
                  val serverRtcpPort: Int,
                  val rtpPort: Int,
                  val rtcpPort: Int,
                  val rtspStreamUrl: String,
                  val destination: String,
                  val source: String,
                  val transport: String,
                  val signalLevel: Int,
                  val signalQuality: Int,
                  val rtspSocket: Any,
                  val rtspSequenceNum: Int = 1) {
  val regexRtspSessionHeader = Pattern.compile("\\s*([^\\s;]+)(;timeout=(\\d+))?")
  val defaultRtspSessionTimeout = 30
  // seconds
  val regexDescribeResponseSignalInfo = Pattern.compile(";tuner=\\d+,(\\d+),(\\d+),(\\d+),") // singleline; ignorecase

  private var _closed = false

  def closed = _closed

  def close() = {
    _closed = true
  }
}
