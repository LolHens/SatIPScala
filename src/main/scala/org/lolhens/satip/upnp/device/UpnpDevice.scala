package org.lolhens.satip.upnp.device

import java.net.{URI, URL}

import org.fourthline.cling.model.meta._
import org.fourthline.cling.model.types.DLNADoc
import org.lolhens.satip.upnp.{Icon, Service}

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
                      val services: List[Service],
                      val embeddedDevices: List[UpnpDevice],
                      val presentationUri: Option[URI],
                      val remoteDetails: Option[RemoteDetails])

object UpnpDevice {
  def apply(device: RemoteDevice): UpnpDevice =
    new UpnpDevice(Udn(device.getIdentity.getUdn.getIdentifierString))(
      deviceType = DeviceType(device.getType),
      friendlyName = device.getDetails().getFriendlyName,
      baseUrl = Option(device.getDetails().getBaseURL),
      dlnaCaps = Option(device.getDetails().getDlnaCaps).toList.flatMap(_.getCaps) ++
        Option(device.getDetails().getSecProductCaps).toList.flatMap(_.getCaps),
      dlnaDocs = device.getDetails().getDlnaDocs.toList,
      manufacturer = Manufacturer(device.getDetails().getManufacturerDetails),
      model = Model(device.getDetails().getModelDetails),
      serialNumber = Option(device.getDetails().getSerialNumber),
      upc = Option(device.getDetails().getUpc),
      icons = device.getIcons.toList.map(Icon(_)),
      services = device.getServices.toList.map(Service(_)),
      embeddedDevices = device.getEmbeddedDevices.toList.map(UpnpDevice(_)),
      presentationUri = Option(device.getDetails().getPresentationURI),
      remoteDetails = Some(RemoteDetails(device.getIdentity))
    )

  def apply(device: LocalDevice): UpnpDevice =
    new UpnpDevice(Udn(device.getIdentity.getUdn.getIdentifierString))(
      deviceType = DeviceType(device.getType),
      friendlyName = device.getDetails().getFriendlyName,
      baseUrl = Option(device.getDetails().getBaseURL),
      dlnaCaps = Option(device.getDetails().getDlnaCaps).toList.flatMap(_.getCaps) ++
        Option(device.getDetails().getSecProductCaps).toList.flatMap(_.getCaps),
      dlnaDocs = device.getDetails().getDlnaDocs.toList,
      manufacturer = Manufacturer(device.getDetails().getManufacturerDetails),
      model = Model(device.getDetails().getModelDetails),
      serialNumber = Option(device.getDetails().getSerialNumber),
      upc = Option(device.getDetails().getUpc),
      icons = device.getIcons.toList.map(Icon(_)),
      services = device.getServices.toList.map(Service(_)),
      embeddedDevices = device.getEmbeddedDevices.toList.map(UpnpDevice(_)),
      presentationUri = Option(device.getDetails().getPresentationURI),
      remoteDetails = None
    )
}
