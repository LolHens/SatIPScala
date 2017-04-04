package org.lolhens.satip.rtsp

import org.lolhens.satip.rtsp.RtspMethod._

/**
  * Created by pierr on 13.11.2016.
  */
abstract case class RtspHeaderField[T](name: String)
                                      (private[rtsp] val supportedMethods: List[RtspMethod]) {

  override def productPrefix: String = getClass.getSimpleName.split('$').head

  abstract class Value(val value: T) {
    def headerField: RtspHeaderField[T] = RtspHeaderField.this

    def string: String

    override def toString: String = s"${headerField.productPrefix}.Value($value)"

    override def hashCode(): Int = string.hashCode

    override def equals(obj: Any): Boolean = string.equals(obj)
  }

  def Value(value: T): Value

  def apply(string: String): Value
}

object RtspHeaderField {

  class StringHeaderField(name: String)
                         (supportedMethods: List[RtspMethod])
    extends RtspHeaderField[String](name)(supportedMethods) {

    class Value(val string: String) extends super.Value(string)

    override def Value(value: String): Value = new Value(value)

    override def apply(string: String): Value = Value(string)
  }

  trait RequestField extends StringHeaderField

  trait ResponseField extends StringHeaderField

  trait GeneralField extends RequestField with ResponseField

  trait EntityField extends StringHeaderField

  private val all = RtspMethod.values

  private def except(methods: List[RtspMethod]) = RtspMethod.values.filterNot(methods.contains)

  private val entity = List(Describe, GetParameter)

  object Accept extends StringHeaderField("Accept")(entity) with RequestField

  object AcceptEncoding extends StringHeaderField("Accept-Encoding")(entity) with RequestField

  object AcceptLanguage extends StringHeaderField("Accept-Language")(all) with RequestField

  object Allow extends StringHeaderField("Allow")(all) with ResponseField

  object Authorization extends StringHeaderField("Authorization")(all) with RequestField

  object Bandwidth extends StringHeaderField("Bandwidth")(all) with RequestField

  object Blocksize extends StringHeaderField("Blocksize")(except(List(Options, Teardown))) with RequestField

  object CacheControl extends StringHeaderField("Cache-Control")(List(Setup)) with GeneralField

  object Conference extends StringHeaderField("Conference")(List(Setup)) with RequestField

  object Connection extends StringHeaderField("Connection")(all) with GeneralField

  object ContentBase extends StringHeaderField("Content-Base")(entity) with EntityField

  object ContentEncoding extends StringHeaderField("Content-Encoding")(List(SetParameter, Describe, Announce)) with EntityField

  object ContentLanguage extends StringHeaderField("Content-Language")(List(Describe, Announce)) with EntityField

  object ContentLength extends StringHeaderField("Content-Length")(List(SetParameter, Announce) ++ entity) with EntityField

  object ContentLocation extends StringHeaderField("Content-Location")(entity) with EntityField

  object ContentType extends StringHeaderField("Content-Type")(List(SetParameter, Announce) ++ entity) with EntityField with ResponseField

  object CSeq extends StringHeaderField("CSeq")(all) with GeneralField

  object Date extends StringHeaderField("Date")(all) with GeneralField

  object Expires extends StringHeaderField("Expires")(List(Describe, Announce)) with EntityField

  object From extends StringHeaderField("From")(all) with RequestField

  object IfModifiedSince extends StringHeaderField("If-Modified-Since")(List(Describe, Setup)) with RequestField

  object LastModified extends StringHeaderField("Last-Modified")(entity) with EntityField

  object ProxyAuthenticate extends StringHeaderField("Proxy-Authenticate")(all)

  object ProxyRequire extends StringHeaderField("Proxy-Require")(all) with RequestField

  object Public extends StringHeaderField("Public")(all) with ResponseField

  object Range extends StringHeaderField("Range")(List(Play, Pause, Record)) with RequestField with ResponseField

  object Referer extends StringHeaderField("Referer")(all) with RequestField

  object Require extends StringHeaderField("Require")(all) with RequestField

  object RetryAfter extends StringHeaderField("Retry-After")(all) with ResponseField

  object RTPInfo extends StringHeaderField("RTP-Info")(List(Play)) with ResponseField

  object Scale extends StringHeaderField("Scale")(List(Play, Record)) with RequestField with ResponseField

  object Session extends StringHeaderField("Session")(except(List(Setup, Options))) with RequestField with ResponseField

  object Server extends StringHeaderField("Server")(all) with ResponseField

  object Speed extends StringHeaderField("Speed")(List(Play)) with RequestField with ResponseField

  object Transport extends StringHeaderField("Transport")(List(Setup)) with RequestField with ResponseField

  object Unsupported extends StringHeaderField("Unsupported")(all) with ResponseField

  object UserAgent extends StringHeaderField("User-Agent")(all) with RequestField

  object Via extends StringHeaderField("Via")(all) with GeneralField

  object WWWAuthenticate extends StringHeaderField("WWW-Authenticate")(all) with ResponseField

  lazy val values: List[RtspHeaderField[_]] = List(
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

  lazy val valuesMap: Map[String, RtspHeaderField[_]] = values.map(e => e.name -> e).toMap
}
