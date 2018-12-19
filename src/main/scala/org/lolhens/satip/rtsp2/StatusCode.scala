package org.lolhens.satip.rtsp2

case class StatusCode(name: String, code: Int) {
  override def toString: String = s"${getClass.getSimpleName.split('$').head}($code)"
}

object StatusCode {

  object Continue extends StatusCode("Continue", 100)

  object Ok extends StatusCode("OK", 200)

  object Created extends StatusCode("Created", 201)

  object LowOnStorageSpace extends StatusCode("Low on Storage Space", 250)

  object MultipleChoices extends StatusCode("Multiple Choices", 300)

  object MovedPermanently extends StatusCode("Moved Permanently", 301)

  object MovedTemporarily extends StatusCode("Moved Temporarily", 302)

  object SeeOther extends StatusCode("See Other", 303)

  object NotModified extends StatusCode("Not Modified", 304)

  object UseProxy extends StatusCode("Use Proxy", 305)

  object BadRequest extends StatusCode("Bad Request", 400)

  object Unauthorized extends StatusCode("Unauthorized", 401)

  object PaymentRequired extends StatusCode("Payment Required", 402)

  object Forbidden extends StatusCode("Forbidden", 403)

  object NotFound extends StatusCode("Not Found", 404)

  object MethodNotAllowed extends StatusCode("Method Not Allowed", 405)

  object NotAcceptable extends StatusCode("Not Acceptable", 406)

  object ProxyAuthenticationRequired extends StatusCode("Proxy Authentication Required", 407)

  object RequestTimeout extends StatusCode("Request Time-out", 408)

  object Gone extends StatusCode("Gone", 410)

  object LengthRequired extends StatusCode("Length Required", 411)

  object PreconditionFailed extends StatusCode("Precondition Failed", 412)

  object RequestEntityTooLarge extends StatusCode("Request Entity Too Large", 413)

  object RequestUriTooLarge extends StatusCode("Request-URI Too Large", 414)

  object UnsupportedMediaType extends StatusCode("Unsupported Media Type", 415)

  object ParameterNotUnderstood extends StatusCode("Parameter Not Understood", 451)

  object ConferenceNotFound extends StatusCode("Conference Not Found", 452)

  object NotEnoughBandwidth extends StatusCode("Not Enough Bandwidth", 453)

  object SessionNotFound extends StatusCode("Session Not Found", 454)

  object MethodNotValidInThisState extends StatusCode("Method Not Valid in This State", 455)

  object HeaderFieldNotValidForThisResource extends StatusCode("Header Field Not Valid for Resource", 456)

  object InvalidRange extends StatusCode("Invalid Range", 457)

  object ParameterIsReadOnly extends StatusCode("Parameter Is Read-Only", 458)

  object AggregateOperationNotAllowed extends StatusCode("Aggregate operation not allowed", 459)

  object OnlyAggregateOperationAllowed extends StatusCode("Only aggregate operation allowed", 460)

  object UnsupportedTransport extends StatusCode("Unsupported transport", 461)

  object DestinationUnreachable extends StatusCode("Destination unreachable", 462)

  object InternalServerError extends StatusCode("Internal Server Error", 500)

  object NotImplemented extends StatusCode("Not Implemented", 501)

  object BadGateway extends StatusCode("Bad Gateway", 502)

  object ServiceUnavailable extends StatusCode("Service Unavailable", 503)

  object GatewayTimeout extends StatusCode("Gateway Time-out", 504)

  object RtspVersionNotSupported extends StatusCode("RTSP Version not supported", 505)

  object OptionNotSupported extends StatusCode("Option not supported", 551)

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

  lazy val valuesMap: Map[Int, StatusCode] = values.map(e => (e.code, e)).toMap

  //val parser: Parser[StatusCode] = digits.!.map((Integer.parseInt(_: String)) andThen (StatusCode.valuesMap(_)))
}