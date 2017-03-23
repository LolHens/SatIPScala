package org.lolhens.satip.upnp

import java.net.URI

import org.seamless.util.MimeType
import scodec.bits.ByteVector

/**
  * Created by u016595 on 23.03.2017.
  */
case class Icon(mimeType: MimeType,
                width: Int,
                height: Int,
                depth: Int,
                uri: URI) {
  def data: ByteVector = ByteVector.empty
}

object Icon {
  def apply(icon: org.fourthline.cling.model.meta.Icon): Icon =
    new Icon(icon.getMimeType, icon.getWidth, icon.getHeight, icon.getDepth, icon.getUri) {
      override def data: ByteVector = Option(icon.getData).map(ByteVector(_)).getOrElse(ByteVector.empty)
    }
}
