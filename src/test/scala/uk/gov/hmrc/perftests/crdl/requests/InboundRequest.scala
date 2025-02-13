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

object InboundRequest {
  def inbound(session: Session): String =
    s"""<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
       |     xmlns:v4="http://xmlns.ec.eu/CallbackService/CSRD2/IReferenceDataExportReceiverCBS/V4"
       |     xmlns:v41="http://xmlns.ec.eu/BusinessObjects/CSRD2/ReferenceDataExportReceiverCBSServiceType/V4"
       |     xmlns:v2="http://xmlns.ec.eu/BusinessObjects/CSRD2/MessageHeaderType/V2">
       |      <soap:Header>
       |        <Action xmlns="http://www.w3.org/2005/08/addressing">CCN2.Service.Customs.Default.CSRD.ReferenceDataExportReceiverCBS/ReceiveReferenceData</Action>
       |        <MessageID xmlns="http://www.w3.org/2005/08/addressing">urn:uuid:fcb0896f-33d1-4542-8f64-1dce8101ca09</MessageID>
       |      </soap:Header>
       |      <soap:Body>
       |        <v4:ReceiveReferenceDataReqMsg>
       |          <v41:MessageHeader>
       |            <v2:messageID>testMessageId123</v2:messageID>
       |            <v2:messageName>test message name</v2:messageName>
       |            <v2:sender>CS/RD2</v2:sender>
       |            <v2:recipient>DPS</v2:recipient>
       |            <v2:timeCreation>2023-10-03T16:00:00</v2:timeCreation>
       |          </v41:MessageHeader>
       |           <v41:TaskIdentifier>${session("TaskID").as[String]}</v41:TaskIdentifier>
       |          <v41:ReceiveReferenceDataRequestResult>${session("CorrelationId").as[String]}</v41:ReceiveReferenceDataRequestResult>
       |        </v4:ReceiveReferenceDataReqMsg>
       |      </soap:Body>
       |    </soap:Envelope>""".stripMargin

  def setupSession: ChainBuilder = {
    exec(session => {
      val newSession = session
        .set("CorrelationId", UUID.randomUUID().toString)
        .set("TaskID", UUID.randomUUID().toString)
      newSession
    })
  }

  val requiredHeaders: Map[String, String] = Map(
    "x-files-included" -> "true",
    "Content-Type"     -> "application/xml",
    "Accept"           -> "application/xml"
  )

  def receiveMessageWrapper: ChainBuilder = {
    exec(http("Receive Initial data from EU")
      .post("/central-reference-data-inbound-orchestrator")
      .headers(requiredHeaders)
      .body(StringBody(session => inbound(session)))
      .check(status.is(202)))
  }
}
