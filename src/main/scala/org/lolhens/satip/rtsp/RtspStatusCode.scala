package org.lolhens.satip.rtsp

/**
  * Created by pierr on 23.10.2016.
  */
case class RtspStatusCode(code: Int) extends AnyVal

object RtspStatusCode {
  val continue = RtspStatusCode(100)
  val ok = RtspStatusCode(200)
  val created = RtspStatusCode(201)
  val lowOnStorageSpace = RtspStatusCode(250)
  val multipleChoices = RtspStatusCode(300)
  val movedPermanently = RtspStatusCode(301)
  val movedTemporarily = RtspStatusCode(302)
  val seeOther = RtspStatusCode(303)
  val notModified = RtspStatusCode(304)
  val useProxy = RtspStatusCode(305)
  val badRequest = RtspStatusCode(400)
  val unauthorized = RtspStatusCode(401)
  val paymentRequired = RtspStatusCode(402)
  val forbidden = RtspStatusCode(403)
  val notFound = RtspStatusCode(404)
  val methodNotAllowed = RtspStatusCode(405)
  val notAcceptable = RtspStatusCode(406)
  val proxyAuthenticationRequired = RtspStatusCode(407)
  val requestTimeout = RtspStatusCode(408)
  val gone = RtspStatusCode(410)
  val lengthRequired = RtspStatusCode(411)
  val preconditionFailed = RtspStatusCode(412)
  val requestEntityTooLarge = RtspStatusCode(413)
  val requestUriTooLarge = RtspStatusCode(414)
  val unsupportedMediaType = RtspStatusCode(415)
  val parameterNotUnderstood = RtspStatusCode(451)
  val conferenceNotFound = RtspStatusCode(452)
  val notEnoughBandwidth = RtspStatusCode(453)
  val sessionNotFound = RtspStatusCode(454)
  val methodNotValidInThisState = RtspStatusCode(455)
  val headerFieldNotValidForThisResource = RtspStatusCode(456)
  val invalidRange = RtspStatusCode(457)
  val parameterIsReadOnly = RtspStatusCode(458)
  val aggregateOperationNotAllowed = RtspStatusCode(459)
  val onlyAggregateOperationAllowed = RtspStatusCode(460)
  val unsupportedTransport = RtspStatusCode(461)
  val destinationUnreachable = RtspStatusCode(462)
  val internalServerError = RtspStatusCode(500)
  val notImplemented = RtspStatusCode(501)
  val badGateway = RtspStatusCode(502)
  val serviceUnavailable = RtspStatusCode(503)
  val gatewayTimeout = RtspStatusCode(504)
  val rtspVersionNotSupported = RtspStatusCode(505)
  val optionNotSupported = RtspStatusCode(551)

  def values = List(
    continue,
    ok,
    created,
    lowOnStorageSpace,
    multipleChoices,
    movedPermanently,
    movedTemporarily,
    seeOther,
    notModified,
    useProxy,
    badRequest,
    unauthorized,
    paymentRequired,
    forbidden,
    notFound,
    methodNotAllowed,
    notAcceptable,
    proxyAuthenticationRequired,
    requestTimeout,
    gone,
    lengthRequired,
    preconditionFailed,
    requestEntityTooLarge,
    requestUriTooLarge,
    unsupportedMediaType,
    parameterNotUnderstood,
    conferenceNotFound,
    notEnoughBandwidth,
    sessionNotFound,
    methodNotValidInThisState,
    headerFieldNotValidForThisResource,
    invalidRange,
    parameterIsReadOnly,
    aggregateOperationNotAllowed,
    onlyAggregateOperationAllowed,
    unsupportedTransport,
    destinationUnreachable,
    internalServerError,
    notImplemented,
    badGateway,
    serviceUnavailable,
    gatewayTimeout,
    rtspVersionNotSupported,
    optionNotSupported
  )

  lazy val valuesMap: Map[Int, RtspStatusCode] = values.map(e => (e.code, e)).toMap
}
