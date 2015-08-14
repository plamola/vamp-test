package traits

import java.time.OffsetDateTime

import io.vamp.common.akka.ExecutionContextProvider
import io.vamp.common.http.RestClient
import io.vamp.pulse.client.{PulseClient, PulseClientProvider}
import io.vamp.pulse.model.{Event, EventQuery, TimeRange}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag


class PulseTestClient(override val url: String)(implicit executionContext: ExecutionContext) extends PulseClient(url) with PulseJsonFormatsProvider {

  val headers = RestClient.jsonHeaders ++ List(RestClient.acceptEncodingIdentity)

  def resetEvents(): Unit = {
    println("resetEvents method no longer exists")
  }

  override def sendEvent(event: Event): Future[Event] = RestClient.post[Event](
    url = s"$url/api/v1/events",
    body = event,
    headers = headers
  )

  override def getEvents(tags: Set[String], from: Option[OffsetDateTime] = None, to: Option[OffsetDateTime] = None, includeLower: Boolean = true, includeUpper: Boolean = true): Future[List[Event]] = {
    query[List[Event]](EventQuery(tags = tags, Some(TimeRange.from(from, to, includeLower, includeUpper))))
  }

  override def query[T <: Any : ClassTag](query: EventQuery)(implicit mf: scala.reflect.Manifest[T]): Future[T] = {
    RestClient.post[T](
      url = s"$url/api/v1/events/get",
      body = query,
      headers = headers
    )
  }


}

trait LocalPulseClientProvider extends PulseClientProvider with ConfigProvider with ExecutionContextProvider {
  override implicit def executionContext: ExecutionContext = {
    global
  }

  override protected val pulseUrl: String = config.getString("endpoints.events.url")

  override lazy val pulseClient = new PulseTestClient(pulseUrl)

}
