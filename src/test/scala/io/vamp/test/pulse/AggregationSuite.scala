package io.vamp.test.pulse

import io.vamp.pulse.model.{ EventQuery, Event }
import io.vamp.test.common.FileAccess
import io.vamp.test.json.PulseJsonFormatsProvider
import org.json4s.native.JsonMethods._
import org.scalatest.tagobjects.Retryable
import org.scalatest.time.{ Millis, Span }
import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

private class AggregationSuite extends FlatSpec
    with LocalPulseClientProvider with Matchers with FileAccess with PulseJsonFormatsProvider with Retries with Cleanup with BeforeAndAfter {

  override protected def before(fun: ⇒ Any): Unit = {
    pulseClient.resetEvents()
    super.before(fun)
  }

  def loadFixtures(file: String) = {
    val eventList = parse(readFile(file)).extract[List[Event]]
    eventList.foreach((ev) ⇒ Await.result(pulseClient.sendEvent(ev), 2 seconds))
  }

  it should "be able to aggregate metrics" taggedAs (Retryable, CleanableTest) in {
    loadFixtures("metricList.json")
    val agg = parse(readFile("metricAggQuery.json")).extract[EventQuery]
    val res = Await.result(pulseClient.query[Map[String, Double]](agg), 2 seconds)
    res shouldBe a[Map[_, Double]]
    res.asInstanceOf[Map[String, Double]]("value") shouldEqual 10D
  }

  ignore should "be able to aggregate events" taggedAs (Retryable, CleanableTest) in {
    loadFixtures("eventList.json")
    val agg = parse(readFile("eventAggQuery.json")).extract[EventQuery]
    val res = Await.result(pulseClient.query[Map[String, Double]](agg), 2 seconds)
    res shouldBe a[Map[_, Double]]
    res.asInstanceOf[Map[String, Double]]("value") shouldEqual 350D
  }

  override def withFixture(test: NoArgTest): Outcome = {
    if (isRetryable(test)) withRetryOnCancel(withRetryOnFailure(Span(5000, Millis))(super.withFixture(test)))
    else super.withFixture(test)
  }
}
