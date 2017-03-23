package org.lolhens.satip.upnp

import org.fourthline.cling.model.meta.{LocalService, RemoteService, StateVariable, Action => ClingAction}
import org.fourthline.cling.model.types.{ServiceId, ServiceType}
import org.lolhens.satip.upnp.Service.{Action, Variable}

/**
  * Created by u016595 on 23.03.2017.
  */
case class Service(id: ServiceId)
                  (val serviceType: ServiceType,
                   val actions: List[Action],
                   val stateVariables: List[Variable]) {
  lazy val actionsMap: Map[String, Action] = actions.map(e => e.name -> e).toMap

  lazy val stateVariablesMap: Map[String, Variable] = stateVariables.map(e => e.name -> e).toMap
}

object Service {
  def apply(service: RemoteService): Service = Service(service.getServiceId)(
    service.getServiceType,
    service.getActions.map(new RemoteAction(_)).toList,
    service.getStateVariables.map(new RemoteVariable(_)).toList
  )

  def apply(service: LocalService[_]): Service = Service(service.getServiceId)(
    service.getServiceType,
    service.getActions.map(new LocalAction(_)).toList,
    service.getStateVariables.map(new LocalVariable(_)).toList
  )

  class Action(val name: String)

  class RemoteAction(action: ClingAction[RemoteService]) extends Action(action.getName)

  class LocalAction(action: ClingAction[LocalService[_]]) extends Action(action.getName)

  class Variable(val name: String)

  class RemoteVariable(variable: StateVariable[RemoteService]) extends Variable(variable.getName)

  class LocalVariable(variable: StateVariable[LocalService[_]]) extends Variable(variable.getName)

}
