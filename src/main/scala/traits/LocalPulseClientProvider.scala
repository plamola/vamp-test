package traits

import io.magnetic.vamp_common.akka.ExecutionContextProvider
import io.magnetic.vamp_common.pulse.PulseClientProvider

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global



trait LocalPulseClientProvider extends PulseClientProvider with ConfigProvider with ExecutionContextProvider{
  override implicit def executionContext: ExecutionContext = {
    global
  }

  override protected val url: String = config.getString("endpoints.pulse.url")
}
