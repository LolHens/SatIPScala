package org.lolhens.satip.upnp.device

import java.net.URI

import org.jupnp.model.meta.ModelDetails

/**
  * Created by u016595 on 23.03.2017.
  */
case class Model(name: String,
                 description: Option[String],
                 number: Option[String],
                 uri: Option[URI])

object Model {
  def apply(modelDetails: ModelDetails): Model = Model(
    modelDetails.getModelName,
    Option(modelDetails.getModelDescription),
    Option(modelDetails.getModelNumber),
    Option(modelDetails.getModelURI)
  )
}
