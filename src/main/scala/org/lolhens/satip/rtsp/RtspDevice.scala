package org.lolhens.satip.rtsp

/**
  * Created by pierr on 23.10.2016.
  */
class RtspDevice(val serverAddress: String,
                 val uniqueDeviceName: String,
                 val friendlyName: String,
                 val rtspSession: Any) {
  private var _disposed = false

  def disposed = _disposed

  def close() = {
    // rtspSession.close()
    _disposed = true
  }
}
