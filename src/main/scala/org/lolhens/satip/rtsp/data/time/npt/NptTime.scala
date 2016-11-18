package org.lolhens.satip.rtsp.data.time.npt

/**
  * Created by pierr on 17.11.2016.
  */
abstract class NptTime {
  override def toString: String
}

object NptTime {
  object Now extends NptTime {
    override def toString: String = "now"
  }

  case class NptSec(seconds: Int)
}
