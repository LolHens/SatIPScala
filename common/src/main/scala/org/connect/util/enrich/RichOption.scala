package org.connect.util.enrich

import scala.language.implicitConversions
import scala.reflect.ClassTag

/**
  * Created by Pierre on 04.03.2016.
  */
class RichOption[A](val self: Option[A]) extends AnyVal {
  def And[B](option: => Option[B]): Option[(A, B)] = self.flatMap(e => option.map(e2 => (e, e2)))

  def XOr[B](option: => Option[B]): Option[Either[A, B]] =
    self.map(e => option.map(e => None).getOrElse(Some(Left(e))))
      .getOrElse(option.map(e => Right(e)))

  def If[B](f: (A) => Boolean): Option[A] = self.filter(f)

  def Then[B](f: A => B): Option[B] = self.map(f)

  def ThenIf[B](f: A => Option[B]): Option[B] = self.flatMap(f)

  def ThenIf[B](f: => Option[B]): Option[B] = ThenIf((e) => f)

  def Else[B >: A](value: => B): B = self.getOrElse(value)

  def ElseIf[B >: A](option: => Option[B]): Option[B] = self.orElse(option)

  def As[T <: A : ClassTag]: Option[T] = self.flatMap {
    case e: T => Some(e)
    case _ => None
  }
}

object RichOption {
  implicit def fromOption[A](option: Option[A]): RichOption[A] = new RichOption(option)
}