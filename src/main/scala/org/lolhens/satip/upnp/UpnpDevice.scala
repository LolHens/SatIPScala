package org.lolhens.satip.upnp

import java.net.{URI, URL}

import org.fourthline.cling.model.meta._
import org.fourthline.cling.model.types.DLNADoc
import org.lolhens.satip.upnp.device.{DeviceType, Manufacturer, Model, Udn}
import org.seamless.util.MimeType
import scodec.bits.ByteVector

/**
  * Created by pierr on 20.03.2017.
  */
case class UpnpDevice(udn: Udn)
                     (val deviceType: DeviceType,
                      val friendlyName: String,
                      val baseUrl: Option[URL],
                      val dlnaCaps: List[String],
                      val dlnaDocs: List[DLNADoc],
                      val manufacturer: Manufacturer,
                      val model: Model,
                      val serialNumber: Option[String],
                      val upc: Option[String],
                      val icons: List[Icon],
                      val services: List[Service[_, _]],
                      val embeddedDevices: List[UpnpDevice],
                      val presentationUri: Option[URI]) {

}

object UpnpDevice {
  def apply[D <: Device[DeviceIdentity, D, Service[D, _]]](device: D): UpnpDevice = {
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
