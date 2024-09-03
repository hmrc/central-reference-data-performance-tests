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

package uk.gov.hmrc.perftests.crdl.simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import uk.gov.hmrc.perftests.crdl.requests._

import scala.concurrent.duration._

class InboundOrchestratorSimulations extends Simulation {
  val scn: ScenarioBuilder = scenario("full journey")
    .exec(InboundRequest.setupSession)
    .exec(InboundRequest.receiveMessageWrapper)
    .pause(1.second)
    .exec(AVScanning.scanningSuccessful)

  setUp(scn.inject(atOnceUsers(1))).protocols(OrchestratorCommon.httpProtocol)
}
