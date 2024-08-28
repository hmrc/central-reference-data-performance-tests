/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.crdl.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.crdl.requests.InboundSdesMessage.{failureJsonBody, successJsonBody, successJsonBodyFromString}
import uk.gov.hmrc.perftests.crdl.requests.InboundSoapMessage.xmlBodyFromID

import java.util.UUID

object SdesCallbackController extends ServicesConfiguration {

  private val baseUrl: String = baseUrlFor("central-reference-data-inbound-orchestrator")
  private val route: String   = "/services/crdl/callback"
  val baseUrlInbound: String  = baseUrlFor("central-reference-data-inbound-orchestrator")

  private val sentHeaders: Map[String, String] = Map(
    "content-type" -> "application/json"
  )
  val id                                       = UUID.randomUUID().toString

  val postSuccessRequestInbound: HttpRequestBuilder =
    http("Post a valid body inbound")
      .post(s"$baseUrlInbound/central-reference-data-inbound-orchestrator")
      .headers(Map("Content-Type" -> "application/xml", "x-files-included" -> "true", "Accept" -> "application/xml"))
      .body(StringBody(xmlBodyFromID(id)))

  val postSuccessRequest: HttpRequestBuilder =
    http("Post a valid body")
      .post(s"$baseUrl$route")
      .headers(sentHeaders)
      .body(StringBody(successJsonBodyFromString(id)))
      .check(status.is(202))

  val postFailureRequest: HttpRequestBuilder =
    http("Post a Failure body")
      .post(s"$baseUrl$route")
      .headers(sentHeaders)
      .body(StringBody(failureJsonBody))
      .check(status.is(202))
}
