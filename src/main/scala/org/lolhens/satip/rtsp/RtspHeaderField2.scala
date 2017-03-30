package org.lolhens.satip.rtsp

import scala.util.{Success, Try}
import scala.util.matching.Regex

/**
  * Created by pierr on 29.03.2017.
  */
case class RtspHeaderField2(name: String, value: String) {
  override def toString: String = s"$name: $value"
}

object RtspHeaderField2 {

  abstract class Factory[T](name: String) {
    def parse(value: String): Try[T]
  }

  trait EntityField extends RtspHeaderField2

  object EntityField {

    class ContentBase(value: String) extends RtspHeaderField2("Content-Base", value) with EntityField

    object ContentBase extends Factory[ContentBase]("Content-Base") {
      def apply(value: String) = new ContentBase(value)

      override def parse(value: String): Try[ContentBase] = Success(ContentBase(value))
    }

    class ContentEncoding(value: String) extends RtspHeaderField2("Content-Encoding", value) with EntityField

    class ContentLanguage(value: String) extends RtspHeaderField2("Content-Language", value) with EntityField

    class ContentLength(length: Int) extends RtspHeaderField2("Content-Length", length.toString) with EntityField

    class ContentLocation(value: String) extends RtspHeaderField2("Content-Location", value) with EntityField

    class ContentType(value: String) extends RtspHeaderField2("Content-Type", value) with EntityField with ResponseField

    class Expires(value: String) extends RtspHeaderField2("Expires", value) with EntityField

    class LastModified(value: String) extends RtspHeaderField2("Last-Modified", value) with EntityField

  }

  trait RequestField extends RtspHeaderField2

  object RequestField {

    class Accept(value: String) extends RtspHeaderField2("Accept", value) with RequestField

    class AcceptEncoding(value: String) extends RtspHeaderField2("Accept-Encoding", value) with RequestField

    class AcceptLanguage(value: String) extends RtspHeaderField2("Accept-Language", value) with RequestField

    class Authorization(value: String) extends RtspHeaderField2("Authorization", value) with RequestField

    class Bandwidth(val bandwidth: Int) extends RtspHeaderField2("Bandwidth", bandwidth.toString) with RequestField

    class Blocksize(val blocksize: Int) extends RtspHeaderField2("Blocksize", blocksize.toString) with RequestField

    class Conference(value: String) extends RtspHeaderField2("Conference", value) with RequestField

    class From(value: String) extends RtspHeaderField2("From", value) with RequestField

    class IfModifiedSince(value: String) extends RtspHeaderField2("If-Modified-Since", value) with RequestField

    class ProxyAuthenticate(value: String) extends RtspHeaderField2("Proxy-Authenticate", value) with RequestField

    class ProxyRequire(value: String) extends RtspHeaderField2("Proxy-Require", value) with RequestField

    class Referer(value: String) extends RtspHeaderField2("Referer", value) with RequestField

    class Require(value: String) extends RtspHeaderField2("Require", value) with RequestField

    class UserAgent(value: String) extends RtspHeaderField2("User-Agent", value) with RequestField

  }

  trait ResponseField extends RtspHeaderField2

  object ResponseField {

    class Allow(val methods: Set[RtspMethod]) extends RtspHeaderField2("Allow", methods.mkString(", ")) with ResponseField

    object Allow {
      def apply(methods: Set[RtspMethod]) = new Allow(methods)

      def parse(value: String): Allow =
        Allow(value.split(Regex.quote(",")).map(_.trim).map(RtspMethod.valuesMap).toSet)
    }

    class Public(value: String) extends RtspHeaderField2("Public", value) with ResponseField

    class RetryAfter(value: String) extends RtspHeaderField2("Retry-After", value) with ResponseField

    class RTPInfo(value: String) extends RtspHeaderField2("RTP-Info", value) with ResponseField

    class Server(value: String) extends RtspHeaderField2("Server", value) with ResponseField

    class Unsupported(value: String) extends RtspHeaderField2("Unsupported", value) with ResponseField

    class WWWAuthenticate(value: String) extends RtspHeaderField2("WWW-Authenticate", value) with ResponseField

  }

  trait GeneralField extends RequestField with ResponseField

  object GeneralField {

    class CacheControl(value: String) extends RtspHeaderField2("Cache-Control", value) with GeneralField

    class Connection(value: String) extends RtspHeaderField2("Connection", value) with GeneralField

    class CSeq(val number: Long) extends RtspHeaderField2("CSeq", number.toString) with GeneralField

    class Date(value: String) extends RtspHeaderField2("Date", value) with GeneralField

    class Range(value: String) extends RtspHeaderField2("Range", value) with GeneralField

    class Scale(scale: Double) extends RtspHeaderField2("Scale", scale.toString) with GeneralField

    class Session(value: String) extends RtspHeaderField2("Session", value) with GeneralField

    class Speed(speed: Double) extends RtspHeaderField2("Speed", speed.toString) with GeneralField

    class Transport(value: String) extends RtspHeaderField2("Transport", value) with GeneralField

    class Via(value: String) extends RtspHeaderField2("Via", value) with GeneralField

  }

}
