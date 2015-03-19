package traits

import io.vamp.common.akka.ExecutionContextProvider
import io.vamp.common.pulse.PulseClientProvider

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global



trait LocalPulseClientProvider extends PulseClientProvider with ConfigProvider with ExecutionContextProvider{
  override implicit def executionContext: ExecutionContext = {
    global
  }

  override protected val url: String = config.getString("endpoints.pulse.url")
}
