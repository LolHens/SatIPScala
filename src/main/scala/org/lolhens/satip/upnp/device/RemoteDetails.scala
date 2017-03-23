package org.lolhens.satip.upnp.device

import java.net.{InetAddress, URL}

import org.fourthline.cling.model.meta.RemoteDeviceIdentity
import scodec.bits.ByteVector

import scala.xml.{Elem, XML}

/**
  * Created by pierr on 23.03.2017.
  */
case class RemoteDetails(descriptorUrl: URL,
                         localInterfaceAddress: InetAddress,
                         remoteInterfaceMac: Option[ByteVector]) {
  lazy val remoteInterfaceAddress: InetAddress = InetAddress.getByName(descriptorUrl.getHost)

  lazy val wakeOnLanBytes: Option[ByteVector] = remoteInterfaceMac.map(mac =>
    ByteVector.concat((0 until 6).map(_ => ByteVector(0xFF))) ++
      ByteVector.concat((0 until 16).map(_ => mac))
  )

  lazy val descriptor: Elem = XML.load(descriptorUrl)
}

object RemoteDetails {
  def apply(remoteDeviceIdentity: RemoteDeviceIdentity): RemoteDetails = RemoteDetails(
    remoteDeviceIdentity.getDescriptorURL,
    remoteDeviceIdentity.getDiscoveredOnLocalAddress,
    Option(remoteDeviceIdentity.getInterfaceMacAddress).map(ByteVector(_))
  )
}
