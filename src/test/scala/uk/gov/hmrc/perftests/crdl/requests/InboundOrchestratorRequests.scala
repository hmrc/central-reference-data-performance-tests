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
import uk.gov.hmrc.perftests.crdl.requests.InboundSoapMessage.xmlBody

object InboundOrchestratorRequests extends ServicesConfiguration {

  private val baseUrl: String = baseUrlFor("central-reference-data-inbound-orchestrator")
  private val route: String   = "/central-reference-data-inbound-orchestrator"

  private val sentHeaders: Map[String, String] = Map(
    "x-files-included" -> "true",
    "Content-Type"     -> "application/xml",
    "Accept"           -> "application/xml"
  )

  val postValidRequest: HttpRequestBuilder =
    http("Post a valid body")
      .post(s"$baseUrl$route")
      .headers(sentHeaders)
      .body(StringBody(xmlBody))
      .check(status.is(202))

  private val missingHeaders: Map[String, String] = Map(
    "Content-Type" -> "application/xml"
  )

  val postBadRequest: HttpRequestBuilder =
    http("Missing Header")
      .post(s"$baseUrl$route")
      .headers(missingHeaders)
      .body(StringBody(xmlBody))
      .check(status.is(400))

  val postWithoutBodyBadRequest: HttpRequestBuilder =
    http("Missing Body")
      .post(s"$baseUrl$route")
      .headers(sentHeaders)
      .body(StringBody(""))
      .check(status.is(400))

}
