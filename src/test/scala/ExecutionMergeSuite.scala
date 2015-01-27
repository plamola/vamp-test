import akka.actor.ActorSystem
import akka.actor.Status.Success
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{ConfigFactory, Config}
import json.SnakifiedJsonSupport
import org.scalatest.{BeforeAndAfter, FunSuite, Suite, Stepwise}
import spray.can.Http
import spray.http.{StatusCodes, StatusCode, HttpResponse, HttpRequest}
import spray.client.pipelining._
import com.typesafe.config
import spray.httpx.SprayJsonSupport._


import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait CustomAsserts {
  protected def assertStatusOk(response: HttpResponse) = {
    val res = response.status match {
      case StatusCodes.Success(_) => true
      case _ => false
    }
    assert(res)
  }
}

case class Deploy(attachTo: String, blueprint: String, clusters: Map[String, Int])
case class ProxyConfig(frontends: Vector[Int], backends: Vector[Int], services: Vector[Int])

object ExecutionJsonProtocol extends SnakifiedJsonSupport {
  implicit val deployFormat = jsonFormat3(Deploy)
  implicit val proxyConfigFormat = jsonFormat3(ProxyConfig)
}




class ExecutionMergeSuite extends FunSuite with BeforeAndAfter with CustomAsserts {
  import ExecutionJsonProtocol._

  val proxyCleanup = ProxyConfig(Vector.empty, Vector.empty, Vector.empty)
  lazy val initialDeployment = Deploy(config.getString("initialBlueprint"), "", Map.empty)
  lazy val mergedBlueprint = config.getString("mergedBlueprint")

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
    val deployReq = Post("/deployment", initialDeployment)

    assertStatusOk(Await.result(proxyPipeline(proxyReq), awaitDuration))
    info(s"Check if proxy is online")

    assertStatusOk(Await.result(vampPipeline(vampReq), awaitDuration))
    info("Check if vamp is online")

    assertStatusOk(Await.result(proxyPipeline(cleanupReq), awaitDuration))
    info("Cleanup proxy")

    assertStatusOk(Await.result(vampPipeline(deployReq), awaitDuration))

  }




}
