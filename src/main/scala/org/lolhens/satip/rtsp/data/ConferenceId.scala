package org.lolhens.satip.rtsp.data

/**
  * Created by pierr on 15.11.2016.
  */
case class ConferenceId(id: String)

object ConferenceId {
  def escape(id: String) = ConferenceId(id) // TODO: url escape
}
