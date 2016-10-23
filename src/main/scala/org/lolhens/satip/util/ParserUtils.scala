package org.lolhens.satip.util

import java.util.regex.Pattern

import fastparse.all._

import scala.util.{Failure, Success, Try}

/**
  * Created by u016595 on 12.09.2016.
  */
object ParserUtils {
  val space = CharPred(char => Pattern.matches("""\s""", s"$char"))
  val s = P(space.rep)
  val s1 = P(space.rep(min = 1))

  val letter = CharIn(('A' to 'Z') ++ ('a' to 'z'))
  val letters = letter.rep(min = 1)

  val digit = CharIn('0' to '9')
  val digits = digit.rep(min = 1)

  val separator = CharIn(List(',', ';'))

  val quote = CharIn(List('\'', '"'))
  val quoted = P(quote ~ (("\\" ~ quote.!) | (!quote ~ AnyChar).!).rep.map(_.mkString) ~ quote)

  // TODO: FIX

  implicit class RichParsed[T](val parsed: Parsed[T]) extends AnyVal {
    def tried: Try[T] = parsed match {
      case Parsed.Success(result, _) =>
        Success(result)

      case failure: Parsed.Failure =>
        Failure(ParseError(failure))
    }
  }

}
