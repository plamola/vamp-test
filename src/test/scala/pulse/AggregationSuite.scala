package pulse

import io.magnetic.vamp_common.pulse.{PulseClient, PulseClientProvider}
import org.scalatest.{Matchers, FlatSpec}
import traits.{LocalPulseClientProvider, FileAccess}
import scala.concurrent.ExecutionContext.Implicits.global
class AggregationSuite extends FlatSpec with LocalPulseClientProvider with Matchers with FileAccess{

}
