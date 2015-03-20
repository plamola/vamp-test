package pulse

import io.vamp.pulse.main.Startup
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, Sequential}

/**
 * Created by lazycoder on 20/03/15.
 */
class PulseSuite extends Sequential (
  new AggregationSuite,
  new StorageSuite
) with BeforeAndAfterAll {
  override protected def beforeAll(): Unit = {
    Startup.main(Array.empty)
    super.beforeAll()
  }

  override protected def afterAll(): Unit = {
    Startup.system.shutdown()
    super.afterAll()
  }
}
