package io.vamp.test.pulse

import io.vamp.common.akka.ExecutionContextProvider
import io.vamp.pulse.client.PulseClientProvider
import io.vamp.test.common.ConfigProvider

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

trait LocalPulseClientProvider extends PulseClientProvider with ConfigProvider with ExecutionContextProvider {
  override implicit def executionContext: ExecutionContext = {
    global
  }

  override protected val pulseUrl: String = config.getString("endpoints.events.url")

  override lazy val pulseClient = new PulseTestClient(pulseUrl)

}
