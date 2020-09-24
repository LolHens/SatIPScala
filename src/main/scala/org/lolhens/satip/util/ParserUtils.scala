package org.lolhens.satip.util

import java.util.regex.Pattern

import fastparse.NoWhitespace._
import fastparse._

import scala.util.Try

/**
 * Created by u016595 on 12.09.2016.
 */
object ParserUtils {
  def isSpace(char: Char): Boolean = Pattern.matches("\\s", String.valueOf(char))


  def space[_: P]: P[Unit] = CharPred(isSpace).opaque("space")

  def s[_: P]: P[Unit] = CharsWhile(isSpace, 0).opaque("spaces?")

  def s1[_: P]: P[Unit] = CharsWhile(isSpace, 1).opaque("spaces")


  def newline[_: P]: P[Unit] = P("\r\n" | "\n" | "\r")


  def isLetter(char: Char): Boolean = (('A' to 'Z') ++ ('a' to 'z') ++ "äöüß").contains(char)


  def letter[_: P]: P[Unit] = CharPred(isLetter).opaque("character")


  def digit[_: P]: P[Unit] = CharPred(CharPredicates.isDigit).opaque("digit")

  def digits[_: P]: P[Unit] = CharsWhile(CharPredicates.isDigit).opaque("digits")


  def isSeparator(char: Char): Boolean = ",;".contains(char)

  def separator[_: P]: P[Unit] = CharPred(isSeparator).opaque("separator")

  def isQuote(char: Char): Boolean = "'\"".contains(char)

  def quote[_: P]: P[Unit] = CharPred(isQuote).opaque("quote")

  def isEscape(char: Char): Boolean = "\\".contains(char)

  def escape[_: P]: P[Unit] = CharPred(isEscape).opaque("escape character")

  def number[_: P]: P[BigDecimal] = ("-".? ~ (digits ~ ("." ~ digits).? | "." ~ digits)).!
    .map(BigDecimal.apply).opaque("number")

  def long[_: P]: P[Long] = ("-".? ~ (digits ~ ("." ~ digits).? | "." ~ digits)).!
    .map(_.toLong).opaque("long")

  def quoted[_: P]: P[String] =
    (quote ~ ((escape ~ (quote.! | escape.!)) | CharPred(char => !(isQuote(char) || isEscape(char))).!).rep
      .map(_.mkString) ~ quote).opaque("quoted text")


  implicit class RichParsed[T](val parsed: Parsed[T]) extends AnyVal {
    def tried: Try[T] = Try(parsed.get.value)
  }

}
