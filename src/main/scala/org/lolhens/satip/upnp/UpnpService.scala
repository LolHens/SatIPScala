package org.lolhens.satip.upnp

import org.fourthline.cling.model.meta._
import org.fourthline.cling.model.types.{ServiceId, ServiceType}

/**
  * Created by u016595 on 23.03.2017.
  */
case class UpnpService(id: ServiceId)
                      (val serviceType: ServiceType,
                       val actions: List[Action[Service[_, _]]],
                       val stateVariables: List[StateVariable[Service[_, _]]]) {
  lazy val actionsMap: Map[String, Action[Service[_, _]]] = actions.map(e => e.getName -> e).toMap

  lazy val stateVariablesMap: Map[String, StateVariable[Service[_, _]]] = stateVariables.map(e => e.getName -> e).toMap
}

object UpnpService {
  def apply(service: Service[_, Service[_, _]]): UpnpService = {
    UpnpService(service.getServiceId)(service.getServiceType, service.getActions.toList, service.getStateVariables.toList)
  }
}
