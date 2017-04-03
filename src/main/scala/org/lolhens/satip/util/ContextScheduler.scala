package org.lolhens.satip.util

import akka.actor.{Actor, ActorContext, Cancellable}
import org.lolhens.satip.util.ContextScheduler.Scheduled

import scala.concurrent.duration.FiniteDuration

/**
  * Created by pierr on 03.04.2017.
  */
trait ContextScheduler extends Actor {
  private var schedules: List[Cancellable] = Nil

  override def aroundReceive(receive: Receive, msg: Any): Unit = {
    super.aroundReceive(new PartialFunction[Any, Unit] {
      override def isDefinedAt(x: Any): Boolean = x match {
        case Scheduled(message, true) =>
          true

        case Scheduled(message, false) =>
          receive.isDefinedAt(message)

        case message =>
          receive.isDefinedAt(message)
      }

      override def apply(v1: Any): Unit = v1 match {
        case Scheduled(message, true) =>

        case Scheduled(message, false) =>
          receive.apply(message)

        case message =>
          receive.apply(message)
      }
    }, msg)
  }

  override def postStop(): Unit = {
    schedules.filterNot(_.isCancelled).foreach(_.cancel())
    super.postStop()
  }

  implicit class RichActorContext(val context: ActorContext) {
    def schedule(initialDelay: FiniteDuration, interval: FiniteDuration, message: Any): Cancellable = {
      val scheduled = Scheduled(message)
      import context.dispatcher
      val cancellable = context.system.scheduler.schedule(initialDelay, interval, self, scheduled)
      val schedule = new Cancellable {
        override def cancel(): Boolean = {
          scheduled.cancel()
          cancellable.cancel()
        }

        override def isCancelled: Boolean = cancellable.isCancelled
      }
      schedules = schedule +: schedules.filterNot(_.isCancelled)
      schedule
    }

    def scheduleOnce(delay: FiniteDuration, message: Any): Cancellable = {
      val scheduled = Scheduled(message)
      import context.dispatcher
      val cancellable = context.system.scheduler.scheduleOnce(delay, self, scheduled)
      val schedule = new Cancellable {
        override def cancel(): Boolean = {
          scheduled.cancel()
          cancellable.cancel()
        }

        override def isCancelled: Boolean = cancellable.isCancelled
      }
      schedules = schedule +: schedules.filterNot(_.isCancelled)
      schedule
    }
  }

}

object ContextScheduler {

  private case class Scheduled(message: Any,
                               @volatile var cancelled: Boolean = false) {
    def cancel(): Unit = cancelled = true
  }

}
