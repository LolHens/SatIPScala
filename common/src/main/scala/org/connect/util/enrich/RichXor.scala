package org.connect.util.enrich

import cats.data.Xor

/**
  * Created by u016595 on 22.09.2016.
  */
class RichXor[L, R](val self: Xor[L, R]) extends AnyVal {
  def And[LL >: L, RR >: R](xor: => Xor[LL, RR]): Xor[LL, (R, RR)] = self.flatMap(e => xor.map(e2 => (e, e2)))

  def Then[B](f: R => B): Xor[L, B] = self.map(f)

  def ThenIf[LL >: L, B](f: R => Xor[LL, B]): Xor[LL, B] = self.flatMap(f)

  def ThenIf[LL >: L, B](f: => Xor[LL, B]): Xor[LL, B] = ThenIf(_ => f)

  def Else[RR >: R](value: L => RR): RR = self match {
    case Xor.Right(result) => result
    case Xor.Left(result) => value(result)
  }

  def ElseIf[LL >: L, RR >: R](xor: => Xor[LL, RR]): Xor[LL, RR] = self.orElse(xor)

  def get: R = self match {
    case Xor.Left(_) => throw new NoSuchElementException("Xor.get")
    case Xor.Right(value) => value
  }
}

object RichXor {
  implicit def fromXor[L, R](xor: Xor[L, R]): RichXor[L, R] = new RichXor(xor)
}