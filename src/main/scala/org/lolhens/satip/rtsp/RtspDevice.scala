package org.lolhens.satip.rtsp

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspDevice(serverAddress: String,
                      uniqueDeviceName: String,
                      friendlyName: String) {
  @volatile private var _closed = false

  def closed: Boolean = _closed

  def close(): Unit = _closed = true
}
