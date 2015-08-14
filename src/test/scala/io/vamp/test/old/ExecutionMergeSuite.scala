package io.vamp.test.old

import akka.actor.ActorSystem

import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfter, FunSuite}
import spray.can.Http
import spray.http.{StatusCodes,HttpResponse}
import spray.client.pipelining._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
import scala.language.postfixOps



import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalatest.Ignore

trait CustomAsserts {
  protected def assertStatusOk(response: HttpResponse) = {
    val res = response.status match {
      case StatusCodes.Success(_) => true
      case _ => false
    }
    assert(res)
  }
}

case class ClusterWeight(weight: Int, name: String)
case class Deploy(blueprint: String, clusters: Vector[ClusterWeight])
case class ProxyConfig(frontends: Vector[Int], backends: Vector[Int], services: Vector[Int])
case class Deployment(id: String, name: String)
case class Executables(executables: Vector[Executable])
case class Executable(name: String, instances: Vector[Instance])
case class Instance(status: String, host: String, ports: List[Int])
case class Execution(id: Int, clusters: Map[String, Executables])
case class MonarchHop(id: Int, host: String, timeStamp: Int)
case class MonarchResponse(hops: List[MonarchHop])


object ExecutionJsonProtocol extends DefaultJsonProtocol {
  implicit val clusterWeightFormat = jsonFormat2(ClusterWeight)
  implicit val deployFormat = jsonFormat2(Deploy)
  implicit val proxyConfigFormat = jsonFormat3(ProxyConfig)
  implicit val deploymentFormat = jsonFormat2(Deployment)
  implicit val instanceFormat = jsonFormat3(Instance)
  implicit val executableFormat = jsonFormat2(Executable)
  implicit val executablesFormat = jsonFormat1(Executables)
  implicit val executionFormat = jsonFormat2(Execution)
  implicit val monarchHopFormat = jsonFormat3(MonarchHop)
  implicit val monarchFormat = jsonFormat1(MonarchResponse)
}





@Ignore class ExecutionMergeSuite extends FunSuite with BeforeAndAfter with CustomAsserts {
  import ExecutionJsonProtocol._

  val proxyCleanup = ProxyConfig(Vector.empty, Vector.empty, Vector.empty)
  lazy val initialDeployment = Deploy(config.getString("initialBlueprint"), Vector.empty)
  lazy val mergeDeployment = Deploy(config.getString("mergedBlueprint"), Vector(ClusterWeight(10, "frontend")))

  implicit val system = ActorSystem()
  import system.dispatcher

  val config = ConfigFactory.load()

  var vampPipeline: SendReceive = _

  var proxyPipeline: SendReceive = _

  val defaultPipeline = sendReceive

  val awaitDuration = 3000 millis

  var executionUrl: String = _

  var deploymentUrl: String = _

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

  after {
    if(executionUrl != null) {
      Await.result(vampPipeline(Delete(executionUrl)), awaitDuration)
    }

    def tryUndeploy(retNum: Int = 0) {


      Await.result(vampPipeline(Delete(deploymentUrl)), awaitDuration)
      Await.result(vampPipeline(Get(deploymentUrl)), awaitDuration).entity.as[Deployment] match {
        case Right(Deployment(id, _)) if id != null => if(retNum > 100) {
          info("Failed to cleanup")
        } else {
          Thread.sleep(1000)
          tryUndeploy(retNum+1)
        }
        case _ => info("cleanup successful")
      }
    }

    if(deploymentUrl != null) {
      tryUndeploy()
    }

  }

  test("vamp and proxy accessible") {
    val proxyReq = Get("/v1/config")
    val vampReq = Get("/")
    val cleanupReq = Post("/v1/config", proxyCleanup)
    val deployReq = Post("/deployment", initialDeployment)
    val executionReq = Post("/deployment/")

    assertStatusOk(Await.result(proxyPipeline(proxyReq), awaitDuration))
    info(s"Check if proxy is online")

    assertStatusOk(Await.result(vampPipeline(vampReq), awaitDuration))
    info("Check if vamp is online")

    assertStatusOk(Await.result(proxyPipeline(cleanupReq), awaitDuration))
    info("Cleanup proxy")

    val deployResult = Await.result(vampPipeline(deployReq), awaitDuration)
    assertStatusOk(deployResult)
    info("Deploy a blueprint")




    val deployment = deployResult.entity.as[Deployment] match {
      case Right(depl) => depl
      case Left(err) => throw new RuntimeException("Error unmarshalling deployment, aborting")
    }

    // TODO: Just query vamp for all the active executions, stop all executions and then deployments
    deploymentUrl = s"/deployment/${deployment.id}"
    var deploymentExecutionUrl = s"/deployment/${deployment.id}/execution"

    assertStatusOk(Await.result(vampPipeline(Post(deploymentExecutionUrl)), awaitDuration))
    info("Start execution")

    info("wait for execution to spin up")
    Thread sleep 1000

    info("Check if frontend instance is in place")
    val monarch1 = tryExecution(deploymentExecutionUrl, 300, "frontend", 1, "monarch1")
    info("Check if frontend instance is accessible and has necessary number of hops")
    assert(tryConnection(monarch1))


    assertStatusOk(Await.result(vampPipeline(Put(s"/deployment/${deployment.id}", mergeDeployment)), awaitDuration))
    info("merge a blueprint to execution")

    info("Check if new frontend instance is in place")
    val monarch4 = tryExecution(deploymentExecutionUrl, 300, "frontend", 2, "monarch4")

    info("Check if new frontend instance is accessible")
    assert(tryConnection(monarch4))

  }

  def tryConnection(instance: Instance): Boolean = {
    Await.result(defaultPipeline(Post(s"http://${instance.host}:${instance.ports.head}/work")), awaitDuration).entity.as[MonarchResponse] match {
      case Right(response) if response.hops.length >= 2 => true
      case Left(err) => false
    }
  }




  def tryExecution(executionUrl: String, retryNum: Int, clusterName: String, executableNum: Int, executableName: String): Instance = {
    @tailrec
    def iter(retryNumber: Int): Instance ={
      val execution = Await.result(vampPipeline(Get(executionUrl)), awaitDuration).entity.as[List[Execution]] match {
        case Right(x :: xs) => x
        case _  => throw new RuntimeException("Unable to get execution")
      }


      //TODO: Make this right, need to return execution id when creating it
      this.executionUrl = s"$executionUrl/${execution.id}"

      val executables = execution.clusters.getOrElse(clusterName, Executables(Vector.empty)).executables

      if(executables.length != executableNum){
        throw new RuntimeException(s"Executable number in cluster '$clusterName' '${executables.length} does not match the expected number '$executableNum'")
      }


      val executable = executables.filter((e) => e.name == executableName) match {
        case x +: xs => x
        case _ => throw new RuntimeException(s"There is no executable of such name $executableName")

      }

      val instance: Instance = executable.instances match {
        case x +: xs => x
        case _ => null
      }

      if(instance != null){
        instance
      } else {
        if(retryNumber > retryNum){
          throw new RuntimeException(s"Executable didn't start up in $retryNum seconds, aborting test.")
        } else {
          Thread sleep 1000
          iter(retryNumber+1)
        }
      }


    }

    iter(0)
  }




}
