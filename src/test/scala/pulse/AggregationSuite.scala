package pulse

import io.magnetic.vamp_common.pulse.api.{EventQuery, Event}
import org.json4s.native.JsonMethods._
import org.scalatest.tagobjects.Retryable
import org.scalatest.time.{Millis, Span}
import org.scalatest._
import traits.{PulseJsonFormatsProvider, LocalPulseClientProvider, FileAccess}
import scala.concurrent.Await
import scala.concurrent.duration._


class AggregationSuite extends FlatSpec
  with LocalPulseClientProvider with Matchers with FileAccess with PulseJsonFormatsProvider with Retries with BeforeAndAfterAll with BeforeAndAfter {




  def loadFixtures(file: String) = {
    val eventList = parse(readFile(file)).extract[List[Event]]
    eventList.foreach((ev) => Await.result(client.sendEvent(ev), 2 seconds))
  }

  it should "be able to aggregate metrics" taggedAs Retryable in {
    loadFixtures("metricList.json")
    val agg = parse(readFile("metricAggQuery.json")).extract[EventQuery]
    val res = Await.result(client.getEvents(agg), 2 seconds)
    res shouldBe a [Map[String, Double]]
    res.asInstanceOf[Map[String, Double]]("value") shouldEqual 350D
  }

  it should "be able to aggregate events" taggedAs Retryable in {
    loadFixtures("eventList.json")
    val agg = parse(readFile("eventAggQuery.json")).extract[EventQuery]
    val res = Await.result(client.getEvents(agg), 2 seconds)
    res shouldBe a [Map[String, Double]]
    res.asInstanceOf[Map[String, Double]]("value") shouldEqual 10D
  }

  override def withFixture(test: NoArgTest): Outcome = {
    if(isRetryable(test)) withRetryOnCancel(withRetryOnFailure(Span(5000, Millis))(super.withFixture(test)))
    else super.withFixture(test)
  }
}
