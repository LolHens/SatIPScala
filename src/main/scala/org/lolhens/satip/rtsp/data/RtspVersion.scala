package org.lolhens.satip.rtsp.data

/**
  * Created by pierr on 13.11.2016.
  */
case class RtspVersion(majorVersion: Int,
                       minorVersion: Int) {
  override def toString: String = s"$majorVersion.$minorVersion"
}
