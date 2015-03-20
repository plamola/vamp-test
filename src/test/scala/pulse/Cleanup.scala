package pulse

import io.vamp.common.pulse.PulseClientProvider
import org.scalatest.time.{Millis, Span}
import org.scalatest._
import io.vamp.pulse.main.Startup
object CleanableTest extends Tag("io.vamp.tags.CleanableTest")

trait Cleanup extends BeforeAndAfterAll{
  this: FlatSpec with Retries with PulseClientProvider =>

  override protected def beforeAll(): Unit = {

    client.resetEvents()
    super.beforeAll()
  }


  override protected def afterAll(): Unit = {
    client.resetEvents()
    super.afterAll()
  }

  abstract override def withFixture(test: NoArgTest): Outcome = {
    if(isCleanable(test)) client.resetEvents()
    if(isRetryable(test)) withRetryOnCancel(withRetryOnFailure(Span(2000, Millis))(super.withFixture(test)))
    else super.withFixture(test)
  }

  def isCleanable(testData: TestData): Boolean = testData.tags.contains(CleanableTest.name)

}
