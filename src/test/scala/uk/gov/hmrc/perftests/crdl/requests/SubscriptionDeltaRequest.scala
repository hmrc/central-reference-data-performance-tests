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
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import java.util.UUID
import scala.io.Source

object SubscriptionDeltaRequest {

  private val xmlTemplate: String =
    Source.fromResource("fixtures/subscription-delta-request.xml").mkString

  def subscriptionDelta(session: Session): String = {
    xmlTemplate.replace(
      "uuid:3a61bf1c-1062-4457-98c7-f7a6945cb215",
      s"uuid:${session("SubscriptionMessageID").as[String]}"
    )
  }

  def setupSession: ChainBuilder =
    exec(session =>
      session.set("SubscriptionMessageID", UUID.randomUUID().toString)
    )

  val requiredHeaders: Map[String, String] = Map(
    "Content-Type" -> "application/xml",
    "Accept"       -> "application/xml"
  )

  def sendSubscriptionDelta: ChainBuilder =
    exec(
      http("Send Subscription Delta Request")
        .post("/central-reference-data-inbound-orchestrator")
        .headers(requiredHeaders)
        .body(StringBody(session => subscriptionDelta(session)))
        .check(status.is(202))
    )
}
