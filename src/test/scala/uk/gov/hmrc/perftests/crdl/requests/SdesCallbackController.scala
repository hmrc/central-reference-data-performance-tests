package uk.gov.hmrc.perftests.crdl.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.crdl.requests.InboundSdesMessage.{failureJsonBody, successJsonBody}


object SdesCallbackController extends ServicesConfiguration {

  private val baseUrl: String = baseUrlFor("central-reference-data-inbound-orchestrator")
  private val route: String = "/services/crdl/callback"

  private val sentHeaders: Map[String, String] = Map(
    "content-type" -> "application/json"
  )

  val postSuccessRequest: HttpRequestBuilder =
    http("Post a valid body")
      .post(s"$baseUrl$route")
      .headers(sentHeaders)
      .body(StringBody(successJsonBody))
      .check(status.is(202))

  val postFailureRequest: HttpRequestBuilder =
    http("Post a Failure body")
      .post(s"$baseUrl$route")
      .headers(sentHeaders)
      .body(StringBody(failureJsonBody))
      .check(status.is(202))
}