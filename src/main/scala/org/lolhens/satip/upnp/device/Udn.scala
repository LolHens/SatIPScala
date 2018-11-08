package org.lolhens.satip.upnp.device

import java.util.UUID

import fastparse.all._
import org.jupnp.model.types.UDN

/**
  * Created by u016595 on 21.03.2017.
  */
case class Udn(identifier: String) {
  override def toString: String = s"uuid:$identifier"
}

object Udn {
  def apply(udn: UDN): Udn = Udn(udn.getIdentifierString)

  def random: Udn = Udn(UUID.randomUUID().toString)

  def parser: Parser[Udn] = ("uuid:" ~ AnyChar.rep.!).map(Udn(_))
}
