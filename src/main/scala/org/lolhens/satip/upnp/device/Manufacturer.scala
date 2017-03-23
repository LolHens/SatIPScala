package org.lolhens.satip.upnp.device

import java.net.URI

import org.fourthline.cling.model.meta.ManufacturerDetails

/**
  * Created by u016595 on 23.03.2017.
  */
case class Manufacturer(name: String,
                        uri: Option[URI])

object Manufacturer {
  def apply(manufacturerDetails: ManufacturerDetails): Manufacturer = Manufacturer(
    manufacturerDetails.getManufacturer,
    Option(manufacturerDetails.getManufacturerURI)
  )
}
