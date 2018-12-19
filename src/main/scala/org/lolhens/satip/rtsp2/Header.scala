package org.lolhens.satip.rtsp2

import cats.Show

case class Header(headerKey: HeaderKey, value: String)

object Header {
  implicit val HeaderShow: Show[Header] = Show.show[Header](header =>
    s"${header.headerKey.name}: ${header.value}"
  )
}
