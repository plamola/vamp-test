package io.vamp.test.pulse

import org.scalatest.time.{Millis, Span}
import org.scalatest._


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
