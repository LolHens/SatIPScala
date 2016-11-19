package org.lolhens.satip.rtsp

/**
  * Created by pierr on 19.11.2016.
  */
case class RtspEntity(entityHeaders: Map[RtspHeaderField.EntityField, String],
                      body: String) {
  def isEmpty = entityHeaders.isEmpty && body == ""
}
