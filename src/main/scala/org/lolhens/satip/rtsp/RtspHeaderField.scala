package org.lolhens.satip.rtsp

import org.lolhens.satip.rtsp.RtspMethod._

/**
  * Created by pierr on 13.11.2016.
  */
abstract case class RtspHeaderField(name: String)
                                   (private[rtsp] val supportedMethods: RtspMethod*) {

  type T

  abstract class Value(val value: T) {
    def headerField: RtspHeaderField = RtspHeaderField.this

    def string: String

    override def toString: String = s"$name: $string" //s"${headerField.productPrefix}.Value($value)"

    override def hashCode(): Int = string.hashCode

    override def equals(obj: Any): Boolean = string.equals(obj)
  }

  abstract class ValueObj {
    def apply(value: T): Value

    def fromString(string: String): Value
  }

  def Value: ValueObj

  def apply(value: T): Value = Value(value)

  override def productPrefix: String = getClass.getName.stripSuffix("$").split("\\.|\\$").last
}

object RtspHeaderField {

  class MappedHeaderField[T1](toValue: String => T1, fromValue: T1 => String)
                             (name: String)
                             (supportedMethods: RtspMethod*)
    extends RtspHeaderField(name)(supportedMethods: _*) {

    type T = T1

    override def Value: ValueObj = new ValueObj {
      override def apply(value: T1): Value = new Value(value) {
        override def string: String = fromValue(value)
      }

      override def fromString(string: String): Value = Value(toValue(string))
    }
  }

  class StringHeaderField(name: String)
                         (supportedMethods: RtspMethod*) extends MappedHeaderField[String](
    string => string,
    value => value
  )(name)(supportedMethods: _*)

  class IntHeaderField(name: String)
                      (supportedMethods: RtspMethod*) extends MappedHeaderField[Int](
    string => string.toInt,
    value => value.toString
  )(name)(supportedMethods: _*)

  class LongHeaderField(name: String)
                       (supportedMethods: RtspMethod*) extends MappedHeaderField[Long](
    string => string.toLong,
    value => value.toString
  )(name)(supportedMethods: _*)

  class DoubleHeaderField(name: String)
                         (supportedMethods: RtspMethod*) extends MappedHeaderField[Double](
    string => string.toDouble,
    value => value.toString
  )(name)(supportedMethods: _*)

  class RtspMethodSetHeaderField(name: String)
                                (supportedMethods: RtspMethod*) extends MappedHeaderField[Set[RtspMethod]](
    string => string.split(',').map(_.trim).map(RtspMethod.valuesMap).toSet,
    value => value.mkString(",")
  )(name)(supportedMethods: _*)

  trait EntityField extends RtspHeaderField

  trait RequestField extends RtspHeaderField

  trait ResponseField extends RtspHeaderField

  trait GeneralField extends RequestField with ResponseField


  private val all = RtspMethod.values

  private def except(methods: RtspMethod*) = RtspMethod.values.filterNot(methods.contains)

  private val entity = List(Describe, GetParameter)


  object Accept extends StringHeaderField("Accept")(entity: _*) with RequestField

  object AcceptEncoding extends StringHeaderField("Accept-Encoding")(entity: _*) with RequestField

  object AcceptLanguage extends StringHeaderField("Accept-Language")(all: _*) with RequestField

  object Allow extends RtspMethodSetHeaderField("Allow")(all: _*) with ResponseField

  object Authorization extends StringHeaderField("Authorization")(all: _*) with RequestField

  object Bandwidth extends IntHeaderField("Bandwidth")(all: _*) with RequestField

  object Blocksize extends IntHeaderField("Blocksize")(except(Options, Teardown): _*) with RequestField

  object CacheControl extends StringHeaderField("Cache-Control")(Setup) with GeneralField

  object Conference extends StringHeaderField("Conference")(Setup) with RequestField

  object Connection extends StringHeaderField("Connection")(all: _*) with GeneralField

  object ContentBase extends StringHeaderField("Content-Base")(entity: _*) with EntityField

  object ContentEncoding extends StringHeaderField("Content-Encoding")(SetParameter, Describe, Announce) with EntityField

  object ContentLanguage extends StringHeaderField("Content-Language")(Describe, Announce) with EntityField

  object ContentLength extends IntHeaderField("Content-Length")(List(SetParameter, Announce) ++ entity: _*) with EntityField

  object ContentLocation extends StringHeaderField("Content-Location")(entity: _*) with EntityField

  object ContentType extends StringHeaderField("Content-Type")(List(SetParameter, Announce) ++ entity: _*) with EntityField with ResponseField

  object CSeq extends LongHeaderField("CSeq")(all: _*) with GeneralField

  object Date extends StringHeaderField("Date")(all: _*) with GeneralField

  object Expires extends StringHeaderField("Expires")(Describe, Announce) with EntityField

  object From extends StringHeaderField("From")(all: _*) with RequestField

  object IfModifiedSince extends StringHeaderField("If-Modified-Since")(Describe, Setup) with RequestField

  object LastModified extends StringHeaderField("Last-Modified")(entity: _*) with EntityField

  object ProxyAuthenticate extends StringHeaderField("Proxy-Authenticate")(all: _*)

  object ProxyRequire extends StringHeaderField("Proxy-Require")(all: _*) with RequestField

  object Public extends RtspMethodSetHeaderField("Public")(all: _*) with ResponseField

  object Range extends StringHeaderField("Range")(Play, Pause, Record) with RequestField with ResponseField

  object Referer extends StringHeaderField("Referer")(all: _*) with RequestField

  object Require extends StringHeaderField("Require")(all: _*) with RequestField

  object RetryAfter extends StringHeaderField("Retry-After")(all: _*) with ResponseField

  object RTPInfo extends StringHeaderField("RTP-Info")(Play) with ResponseField

  object Scale extends DoubleHeaderField("Scale")(Play, Record) with RequestField with ResponseField

  object Session extends StringHeaderField("Session")(except(Setup, Options): _*) with RequestField with ResponseField

  object Server extends StringHeaderField("Server")(all: _*) with ResponseField

  object Speed extends DoubleHeaderField("Speed")(Play) with RequestField with ResponseField

  object Transport extends StringHeaderField("Transport")(Setup) with RequestField with ResponseField

  object Unsupported extends StringHeaderField("Unsupported")(all: _*) with ResponseField

  object UserAgent extends StringHeaderField("User-Agent")(all: _*) with RequestField

  object Via extends StringHeaderField("Via")(all: _*) with GeneralField

  object WWWAuthenticate extends StringHeaderField("WWW-Authenticate")(all: _*) with ResponseField

  lazy val values: List[RtspHeaderField] = List(
    Accept,
    AcceptEncoding,
    AcceptLanguage,
    Allow,
    Authorization,
    Bandwidth,
    Blocksize,
    CacheControl,
    Conference,
    Connection,
    ContentBase,
    ContentEncoding,
    ContentLanguage,
    ContentLength,
    ContentLocation,
    ContentType,
    CSeq,
    Date,
    Expires,
    From,
    IfModifiedSince,
    LastModified,
    ProxyAuthenticate,
    ProxyRequire,
    Public,
    Range,
    Referer,
    Require,
    RetryAfter,
    RTPInfo,
    Scale,
    Session,
    Server,
    Speed,
    Transport,
    Unsupported,
    UserAgent,
    Via,
    WWWAuthenticate
  )

  lazy val valuesMap: Map[String, RtspHeaderField] = values.map(e => e.name -> e).toMap
}
