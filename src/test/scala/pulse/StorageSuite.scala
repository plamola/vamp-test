package pulse

import com.typesafe.config.ConfigFactory
import io.magnetic.vamp_common.pulse.PulseClientProvider
import io.magnetic.vamp_common.pulse.api.{EventQuery, Event}
import org.json4s.native.JsonMethods._
import org.scalatest.tagobjects.Retryable
import org.scalatest.time.{Millis, Span}
import org.scalatest.{Outcome, Retries, Matchers, FlatSpec}
import traits.{PulseJsonFormatsProvider, LocalPulseClientProvider, FileAccess}
import scala.concurrent.Await
import scala.concurrent.duration._
import io.magnetic.vamp_common.json.Serializers

class StorageSuite extends FlatSpec with LocalPulseClientProvider with Matchers with FileAccess with PulseJsonFormatsProvider with Retries {


  it should "be able to send a metric to pulse"  in {
    val event = parse(readFile("metric.json")).extract[Event]
    val res = Await.result(client.sendEvent(event), 2 seconds)
    res shouldBe an [Event]
    // Then we sleep for the results to appear in ElasticSearch, it's not fast, unfortunately
  }

  it should "be able to query recently sent metric from pulse" taggedAs Retryable in {
      val query = parse(readFile("metricQuery.json")).extract[EventQuery]
      val res = Await.result(client.getEvents(query), 2 seconds)
      res.asInstanceOf[List[_]] should not be empty
  }

  it should "be able to send a json blob to pulse"  in {
    val event = parse(readFile("blob.json")).extract[Event]
    val res = Await.result(client.sendEvent(event), 2 seconds)
    res shouldBe an [Event]
    // Then we sleep for the results to appear in ElasticSearch, it's not fast, unfortunately
  }


  it should "be able to query recently sent blob from pulse" taggedAs Retryable in {
    val query = parse(readFile("blobQuery.json")).extract[EventQuery]
    val res =  Await.result(client.getEvents(query), 2 seconds)
    res.asInstanceOf[List[_]] should not be empty
  }


  it should "be able to send a typed event to pulse" in {
    val event = parse(readFile("event.json")).extract[Event]
    val res = Await.result(client.sendEvent(event), 2 seconds)
    res shouldBe an [Event]
    // Then we sleep for the results to appear in ElasticSearch, it's not fast, unfortunately
  }


  it should "be able to query recently sent event from pulse" taggedAs Retryable in {
    val query = parse(readFile("eventQuery.json")).extract[EventQuery]
    val res =  Await.result(client.getEvents(query), 2 seconds)
    res.asInstanceOf[List[_]] should not be empty
  }

  override def withFixture(test: NoArgTest): Outcome = {
    if(isRetryable(test)) withRetryOnCancel(withRetryOnFailure(Span(2000, Millis))(super.withFixture(test)))
    else super.withFixture(test)
  }
}
