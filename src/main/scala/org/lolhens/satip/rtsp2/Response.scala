package org.lolhens.satip.rtsp2

import org.lolhens.satip.rtsp.data.RtspVersion

case class Response(statusCode: StatusCode,
                    reason: String,
                    headers: Headers,
                    entity: Option[String],
                    version: RtspVersion)