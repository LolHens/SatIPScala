package org.lolhens.satip.rtsp

/**
  * Created by pierr on 19.11.2016.
  */
case class RtspEntity(entityHeaders: List[RtspHeaderField.EntityField#Value],
                      body: String) {
  def isEmpty: Boolean = entityHeaders.isEmpty && body == ""
}
