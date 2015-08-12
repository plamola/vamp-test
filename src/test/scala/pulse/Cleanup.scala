package pulse

//import io.vamp.pulse.client.PulseClientProvider
import org.scalatest.time.{Millis, Span}
import org.scalatest._
import traits.LocalPulseClientProvider

object CleanableTest extends Tag("io.vamp.tags.CleanableTest")

trait Cleanup extends BeforeAndAfterAll{
  this: FlatSpec with Retries with LocalPulseClientProvider =>

  override protected def beforeAll(): Unit = {

    pulseClient.resetEvents()
    super.beforeAll()
  }


  override protected def afterAll(): Unit = {
    pulseClient.resetEvents()
    super.afterAll()
  }

  abstract override def withFixture(test: NoArgTest): Outcome = {
    if(isCleanable(test)) pulseClient.resetEvents()
    if(isRetryable(test)) withRetryOnCancel(withRetryOnFailure(Span(2000, Millis))(super.withFixture(test)))
    else super.withFixture(test)
  }

  def isCleanable(testData: TestData): Boolean = testData.tags.contains(CleanableTest.name)

}
