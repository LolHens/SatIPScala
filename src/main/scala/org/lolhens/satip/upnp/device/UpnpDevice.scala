package org.lolhens.satip.upnp.device

import java.net.{URI, URL}

import org.fourthline.cling.model.meta._
import org.fourthline.cling.model.types.DLNADoc
import org.lolhens.satip.upnp.{Icon, UpnpService}

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
                      val services: List[UpnpService],
                      val embeddedDevices: List[UpnpDevice],
                      val presentationUri: Option[URI])

object UpnpDevice {
  def apply2[D <: Device[DeviceIdentity, D, Service[D, Service[_, _]]]](device: D): UpnpDevice = {
    new UpnpDevice(Udn(device.getIdentity.getUdn.getIdentifierString))(
      deviceType = DeviceType(device.getType),
      friendlyName = device.getDetails.getFriendlyName,
      baseUrl = Option(device.getDetails.getBaseURL),
      dlnaCaps = Option(device.getDetails.getDlnaCaps).toList.flatMap(_.getCaps) ++
        Option(device.getDetails.getSecProductCaps).toList.flatMap(_.getCaps),
      dlnaDocs = device.getDetails.getDlnaDocs.toList,
      manufacturer = Manufacturer(device.getDetails.getManufacturerDetails),
      model = Model(device.getDetails.getModelDetails),
      serialNumber = Option(device.getDetails.getSerialNumber),
      upc = Option(device.getDetails.getUpc),
      icons = device.getIcons.toList.map(Icon(_)),
      services = device.getServices.toList.map(UpnpService(_)),
      embeddedDevices = device.getEmbeddedDevices.toList.map(UpnpDevice.apply2(_)),
      presentationUri = Option(device.getDetails.getPresentationURI)
    )
  }

  def apply3(localDevice: LocalDevice) = apply2(localDevice)

  def apply3(remoteDevice: RemoteDevice) = apply2(remoteDevice)
}
