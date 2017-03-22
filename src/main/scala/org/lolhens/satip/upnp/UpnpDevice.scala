package org.lolhens.satip.upnp

import java.net.{URI, URL}

import org.fourthline.cling.model.meta._
import org.fourthline.cling.model.types.DLNADoc
import org.lolhens.satip.upnp.device.Udn

/**
  * Created by pierr on 20.03.2017.
  */
case class UpnpDevice(udn: Udn)
                     (val deviceType: DeviceType,
                     val embeddedDevices: List[UpnpDevice],
                     val services: List[Service[_, _]],
                     val details: UpnpDevice.Details) {

}

object UpnpDevice {
  def apply[D <: Device[DeviceIdentity, D, Service[_, _]]](device: D): UpnpDevice = {
    new UpnpDevice(
       Udn(device.getIdentity.getUdn.getIdentifierString),
       DeviceType(device.getType),
       device.isInstanceOf[LocalDevice]
    )(
      device.getEmbeddedDevices.toList,
      device.getServices,
      Details(device.getDetails)
    )
  }

  case class Details(friendlyName: String,
                     baseUrl: Option[URL],
                     dlnaCaps: List[String],
                     dlnaDocs: List[DLNADoc],
                     manufacturerDetails: Option[ManufacturerDetails],
                     modelDetails: Option[ModelDetails],
                     presentationUri: Option[URI],
                     serialNumber: Option[String],
                     upc: Option[String])

  object Details {
    def apply(deviceDetails: DeviceDetails): Details = Details(
      friendlyName = deviceDetails.getFriendlyName,
      baseUrl = Option(deviceDetails.getBaseURL),
      dlnaCaps = Option(deviceDetails.getDlnaCaps).flatMap(_.getCaps.toSeq).toList ++
        Option(deviceDetails.getSecProductCaps).flatMap(_.getCaps.toSeq).toList,
      dlnaDocs = deviceDetails.getDlnaDocs.toList,
      manufacturerDetails = Option(deviceDetails.getManufacturerDetails),
      modelDetails = Option(deviceDetails.getModelDetails),
      presentationUri = Option(deviceDetails.getPresentationURI),
      serialNumber = Option(deviceDetails.getSerialNumber),
      upc = Option(deviceDetails.getUpc)
    )
  }

}
