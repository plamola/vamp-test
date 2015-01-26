import akka.actor.ActorSystem
import akka.actor.Status.Success
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{ConfigFactory, Config}
import org.scalatest.{BeforeAndAfter, FunSuite, Suite, Stepwise}
import spray.can.Http
import spray.http.{StatusCodes, StatusCode, HttpResponse, HttpRequest}
import spray.client.pipelining._
import com.typesafe.config
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol


import scala.concurrent.{Await, Future}
import scala.concurrent.duration._



class ExecutionMergeSuite extends FunSuite with BeforeAndAfter{

  case class Deploy(attachTo: String, blueprint: String, clusters: Map[String, Int])
  case class ProxyConfig(frontends: Vector[Int], backends: Vector[Int], services: Vector[Int])

  val proxyCleanup = ProxyConfig(Vector.empty, Vector.empty, Vector.empty)

  object ExecutionJsonProtocol extends DefaultJsonProtocol {
    implicit val deployFormat = jsonFormat3(Deploy)
    implicit val proxyConfigFormat = jsonFormat3(ProxyConfig)
  }

  import ExecutionJsonProtocol._

  private def assertStatusOk(response: HttpResponse) = {
    val res = response.status match {
      case StatusCodes.Success(_) => true
      case _ => false
    }
    assert(res)
  }
  implicit val system = ActorSystem()
  import system.dispatcher

  val config = ConfigFactory.load()

  var vampPipeline: SendReceive = _

  var proxyPipeline: SendReceive = _

  val awaitDuration = 3000 millis

  implicit val timeout = Timeout(3000 millis)

  before {
    def initialisePipeline(endpointName: String): SendReceive = {
      val result = Await.result(IO(Http) ? Http.HostConnectorSetup(config.getString(s"endpoints.$endpointName.host"), port = config.getInt(s"endpoints.$endpointName.port")), awaitDuration)
       result match {
        case Http.HostConnectorInfo(connector, _) => sendReceive(connector)
        case _ => throw new RuntimeException("Unable to acquire connector")
      }
    }

    vampPipeline = initialisePipeline("vamp")
    proxyPipeline = initialisePipeline("haproxy")
  }

  test("vamp and proxy accessible") {
    val proxyReq = Get("/v1/config")
    val vampReq = Get("/")
    val cleanupReq = Post("/v1/config", proxyCleanup)

    assertStatusOk(Await.result(proxyPipeline(proxyReq), awaitDuration))
    info(s"Check if proxy is online")
    assertStatusOk(Await.result(vampPipeline(vampReq), awaitDuration))
    info("Check if vamp is online")
    assertStatusOk(Await.result(proxyPipeline(cleanupReq), awaitDuration))
    info("Cleanup proxy")



  }




}
