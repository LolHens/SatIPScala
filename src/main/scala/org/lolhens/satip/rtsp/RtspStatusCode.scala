package org.lolhens.satip.rtsp

import fastparse._
import org.lolhens.satip.util.ParserUtils._

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspStatusCode(name: String, code: Int) {
  override def toString: String = s"${getClass.getSimpleName.split('$').head}($code)"
}

object RtspStatusCode {

  object Continue extends RtspStatusCode("Continue", 100)

  object Ok extends RtspStatusCode("OK", 200)

  object Created extends RtspStatusCode("Created", 201)

  object LowOnStorageSpace extends RtspStatusCode("Low on Storage Space", 250)

  object MultipleChoices extends RtspStatusCode("Multiple Choices", 300)

  object MovedPermanently extends RtspStatusCode("Moved Permanently", 301)

  object MovedTemporarily extends RtspStatusCode("Moved Temporarily", 302)

  object SeeOther extends RtspStatusCode("See Other", 303)

  object NotModified extends RtspStatusCode("Not Modified", 304)

  object UseProxy extends RtspStatusCode("Use Proxy", 305)

  object BadRequest extends RtspStatusCode("Bad Request", 400)

  object Unauthorized extends RtspStatusCode("Unauthorized", 401)

  object PaymentRequired extends RtspStatusCode("Payment Required", 402)

  object Forbidden extends RtspStatusCode("Forbidden", 403)

  object NotFound extends RtspStatusCode("Not Found", 404)

  object MethodNotAllowed extends RtspStatusCode("Method Not Allowed", 405)

  object NotAcceptable extends RtspStatusCode("Not Acceptable", 406)

  object ProxyAuthenticationRequired extends RtspStatusCode("Proxy Authentication Required", 407)

  object RequestTimeout extends RtspStatusCode("Request Time-out", 408)

  object Gone extends RtspStatusCode("Gone", 410)

  object LengthRequired extends RtspStatusCode("Length Required", 411)

  object PreconditionFailed extends RtspStatusCode("Precondition Failed", 412)

  object RequestEntityTooLarge extends RtspStatusCode("Request Entity Too Large", 413)

  object RequestUriTooLarge extends RtspStatusCode("Request-URI Too Large", 414)

  object UnsupportedMediaType extends RtspStatusCode("Unsupported Media Type", 415)

  object ParameterNotUnderstood extends RtspStatusCode("Parameter Not Understood", 451)

  object ConferenceNotFound extends RtspStatusCode("Conference Not Found", 452)

  object NotEnoughBandwidth extends RtspStatusCode("Not Enough Bandwidth", 453)

  object SessionNotFound extends RtspStatusCode("Session Not Found", 454)

  object MethodNotValidInThisState extends RtspStatusCode("Method Not Valid in This State", 455)

  object HeaderFieldNotValidForThisResource extends RtspStatusCode("Header Field Not Valid for Resource", 456)

  object InvalidRange extends RtspStatusCode("Invalid Range", 457)

  object ParameterIsReadOnly extends RtspStatusCode("Parameter Is Read-Only", 458)

  object AggregateOperationNotAllowed extends RtspStatusCode("Aggregate operation not allowed", 459)

  object OnlyAggregateOperationAllowed extends RtspStatusCode("Only aggregate operation allowed", 460)

  object UnsupportedTransport extends RtspStatusCode("Unsupported transport", 461)

  object DestinationUnreachable extends RtspStatusCode("Destination unreachable", 462)

  object InternalServerError extends RtspStatusCode("Internal Server Error", 500)

  object NotImplemented extends RtspStatusCode("Not Implemented", 501)

  object BadGateway extends RtspStatusCode("Bad Gateway", 502)

  object ServiceUnavailable extends RtspStatusCode("Service Unavailable", 503)

  object GatewayTimeout extends RtspStatusCode("Gateway Time-out", 504)

  object RtspVersionNotSupported extends RtspStatusCode("RTSP Version not supported", 505)

  object OptionNotSupported extends RtspStatusCode("Option not supported", 551)

  lazy val values = List(
    Continue,
    Ok,
    Created,
    LowOnStorageSpace,
    MultipleChoices,
    MovedPermanently,
    MovedTemporarily,
    SeeOther,
    NotModified,
    UseProxy,
    BadRequest,
    Unauthorized,
    PaymentRequired,
    Forbidden,
    NotFound,
    MethodNotAllowed,
    NotAcceptable,
    ProxyAuthenticationRequired,
    RequestTimeout,
    Gone,
    LengthRequired,
    PreconditionFailed,
    RequestEntityTooLarge,
    RequestUriTooLarge,
    UnsupportedMediaType,
    ParameterNotUnderstood,
    ConferenceNotFound,
    NotEnoughBandwidth,
    SessionNotFound,
    MethodNotValidInThisState,
    HeaderFieldNotValidForThisResource,
    InvalidRange,
    ParameterIsReadOnly,
    AggregateOperationNotAllowed,
    OnlyAggregateOperationAllowed,
    UnsupportedTransport,
    DestinationUnreachable,
    InternalServerError,
    NotImplemented,
    BadGateway,
    ServiceUnavailable,
    GatewayTimeout,
    RtspVersionNotSupported,
    OptionNotSupported
  )

  lazy val valuesMap: Map[Int, RtspStatusCode] = values.map(e => (e.code, e)).toMap

  def parser[_: P]: P[RtspStatusCode] = digits.!.map((Integer.parseInt(_: String)) andThen (RtspStatusCode.valuesMap(_)))
}
