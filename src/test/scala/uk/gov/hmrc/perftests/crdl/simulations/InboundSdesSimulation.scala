package uk.gov.hmrc.perftests.crdl.simulations

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.crdl.requests.SdesCallbackController.{postFailureRequest, postSuccessRequest}

class InboundSdesSimulation extends PerformanceTestRunner {
  setup("post-valid-body", "Post Valid Body") withRequests postSuccessRequest
  setup("failure-body", "Failure Body") withRequests postFailureRequest

  runSimulation()
}
