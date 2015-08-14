package io.vamp.test.core.api

import io.vamp.common.http.RestClient
import io.vamp.core.model.artifact.{Blueprint, Deployment, DeploymentService}
import io.vamp.core.model.reader.DeploymentReader
import io.vamp.test.model.DeployableApp
import traits.YamlUtils

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps


trait DeploymentTools extends CoreTools with YamlUtils {

  implicit val host: String = config.getString("endpoints.router.host")


  def allDeploymentServiceStates(deployment: Deployment): List[DeploymentService.State] = for {
    cluster <- deployment.clusters
    service <- cluster.services
  } yield service.state

  def areAllServicesDeployed(deployment: Deployment): Boolean = allDeploymentServiceStates(deployment = deployment).count(state => !state.isInstanceOf[DeploymentService.Deployed]) == 0d


  def getDeploymentbyNameViaRestAPI(name: String): Option[Deployment] = sendAndWaitYaml(request = s"GET $url/api/v1/deployments/$name") match {
    case Some(deployment) => Some(DeploymentReader.read(deployment))
    case None => None
  }

  def deployViaRestAPI(blueprint: Blueprint): Option[Deployment] = sendAndWaitYaml(request = s"POST $url/api/v1/deployments", Some(artifactToYaml(blueprint))) match {
    case Some(x) => Some(DeploymentReader.read(x))
    case None => None
  }


  def deploymentUpdateViaRestAPI(blueprint: Blueprint, deploymentName: String): Option[Deployment] = sendAndWaitYaml(request = s"PUT $url/api/v1/deployments/$deploymentName", Some(artifactToYaml(blueprint))) match {
    case Some(x) => Some(DeploymentReader.read(x))
    case None => None
  }


  def undeployViaRestAPI(deployment: Deployment): Option[Deployment] = sendAndWaitYaml(request = s"DELETE $url/api/v1/deployments/${deployment.name}", Some(artifactToYaml(deployment))) match {
    case Some(updatedDeployment) => Some(DeploymentReader.read(updatedDeployment))
    case None => None
  }


  def getAndVerifyDeploymentStates(deploymentName: String, myApp: DeployableApp): Future[Boolean] = Future {
    Poll {
      getDeploymentbyNameViaRestAPI(deploymentName)
    } until (runningDeploymentOption => runningDeploymentOption.get.isInstanceOf[Deployment] &&
      allDeploymentServiceStates(runningDeploymentOption.get).size == myApp.nrOfServices &&
      areAllServicesDeployed(deployment = runningDeploymentOption.get)
      )
    true
  }


  def verifyDeploymentIsRemoved(deploymentName: String): Future[Boolean] = Future {
    Poll {
      getDeploymentbyNameViaRestAPI(deploymentName)
    } until (deploymentOption => deploymentOption.isEmpty)
    true
  }

  def getApplicationPage(myApp: DeployableApp): String = Await.result(RestClient.get[String](appCheckUrl(myApp)), myApp.appWaitTime seconds)

  private def appCheckUrl(myApp: DeployableApp): String = s"http://$host:${myApp.checkPort}/${myApp.checkUri}"


}

class Poll[A](body: => A) {
  def until(cond: A => Boolean): A = {
    val result = body
    if (cond(result)) result else until(cond)
  }
}

object Poll {
  def apply[A](body: => A) = new Poll({
    //println("polling...")
    Thread.sleep(200L)
    body
  })


}



