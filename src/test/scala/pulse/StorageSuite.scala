package pulse




import io.vamp.pulse.model.{EventQuery, Event}

import org.json4s.native.JsonMethods._

import org.scalatest.tagobjects.Retryable

import org.scalatest._
import traits.{PulseJsonFormatsProvider, LocalPulseClientProvider, FileAccess}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps



private class StorageSuite extends FlatSpec with LocalPulseClientProvider with Matchers with FileAccess  with PulseJsonFormatsProvider with Cleanup with Retries{


  it should "be able to send a metric to pulse" taggedAs CleanableTest in {
    val event = parse(readFile("metric.json")).extract[Event]
    val res = Await.result(pulseClient.sendEvent(event), 2 seconds)
    res shouldBe an [Event]
    // Then we sleep for the results to appear in ElasticSearch, it's not fast, unfortunately
  }

  it should "be able to query recently sent metric from pulse" taggedAs Retryable in {
      val query = parse(readFile("metricQuery.json")).extract[EventQuery]
      val res = Await.result(pulseClient.getEvents(query.tags), 2 seconds)
      res.asInstanceOf[List[_]] should not be empty
  }

  it should "be able to send a json blob to pulse"  taggedAs CleanableTest in {
    val event = parse(readFile("blob.json")).extract[Event]
    val res = Await.result(pulseClient.sendEvent(event), 2 seconds)
    res shouldBe an [Event]
    // Then we sleep for the results to appear in ElasticSearch, it's not fast, unfortunately
  }


//  it should "be able to query recently sent blob from pulse" taggedAs Retryable in {
//    val query = parse(readFile("blobQuery.json")).extract[EventQuery]
//    val res =  Await.result(pulseClient.getEvents(query.tags), 2 seconds)
//    res.asInstanceOf[List[Event]] should not be empty
//  }


  it should "be able to send a typed event to pulse" taggedAs CleanableTest in {
    val event = parse(readFile("event.json")).extract[Event]
    val res = Await.result(pulseClient.sendEvent(event), 2 seconds)
    res shouldBe an [Event]
    // Then we sleep for the results to appear in ElasticSearch, it's not fast, unfortunately
  }


  it should "be able to query recently sent event from pulse" taggedAs Retryable in {
    val query = parse(readFile("eventQuery.json")).extract[EventQuery]
    val res =  Await.result(pulseClient.getEvents(query.tags), 2 seconds)
    res.asInstanceOf[List[Event]] should not be empty
  }


}
