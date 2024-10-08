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
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, status}

object AVScanning {
  val requiredHeaders: Map[String, String] = Map(
    "Content-Type"     -> "application/json"
  )

  def scanningSuccessMessage(session: Session): String =
    s"""{
       |	"notification": "FileReceived",
       |	"filename": "${session("CorrelationId").as[String]}.zip",
       |	"checksumAlgorithm": "md5",
       |	"checksum": "894bed34007114b82fa39e05197f9eec",
       |	"correlationID": "${session("CorrelationId").as[String]}",
       |	"dateTime": "2020-11-09T16:48:21.659Z",
       |	"properties": [{
       |		"name": "name1",
       |		"value": "value1"
       |	}, {
       |		"name": "name2",
       |		"value": "value2"
       |	}]
       |}""".stripMargin

  def scanningSuccessful: ChainBuilder = {
    exec(http("Receive result of AV scanning from SDES")
      .post("/central-reference-data-inbound-orchestrator/services/crdl/callback")
      .headers(requiredHeaders)
      .body(StringBody(session => scanningSuccessMessage(session)))
      .check(status.is(202)))
  }
}
