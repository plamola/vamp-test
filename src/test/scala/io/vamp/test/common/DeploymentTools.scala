package io.vamp.test.common

import io.vamp.common.http.RestClient
import io.vamp.core.model.artifact.{Blueprint, Deployment, DeploymentService}
import io.vamp.test.model.DeployableApp
import traits.{ConfigProvider, RestSupport}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps


trait DeploymentTools extends RestSupport with ConfigProvider {

  implicit def executionContext: ExecutionContext = {
    global
  }

  implicit val host: String = config.getString("endpoints.router.host")

  def interfaceDescription: String

  def getDeploymentbyName(name: String): Option[Deployment]

  def deploy(blueprint: Blueprint): Option[Deployment]

  def deploymentUpdate(blueprint: Blueprint, deploymentName: String): Option[Deployment]

  def undeploy(deployment: Deployment): Option[Deployment]


  def getAndVerifyDeploymentStates(deploymentName: String, myApp: DeployableApp): Future[Boolean] = Future {
    Poll {
      getDeploymentbyName(deploymentName)
    } until (runningDeploymentOption => runningDeploymentOption.get.isInstanceOf[Deployment] &&
      allDeploymentServiceStates(runningDeploymentOption.get).size == myApp.nrOfServices &&
      areAllServicesDeployed(deployment = runningDeploymentOption.get)
      )
    true
  }

  def verifyDeploymentIsRemoved(deploymentName: String): Future[Boolean] = Future {
    Poll {
      getDeploymentbyName(deploymentName)
    } until (deploymentOption => deploymentOption.isEmpty)
    true
  }

  def allDeploymentServiceStates(deployment: Deployment): List[DeploymentService.State] = for {
    cluster <- deployment.clusters
    service <- cluster.services
  } yield service.state


  def areAllServicesDeployed(deployment: Deployment): Boolean = allDeploymentServiceStates(deployment = deployment).count(state => !state.isInstanceOf[DeploymentService.Deployed]) == 0d

  def getApplicationPage(myApp: DeployableApp, headers: List[(String, String)] = List.empty): String = Await.result(
    RestClient.get[String](
      url = appCheckUrl(myApp),
      headers = headers
    ), myApp.appWaitTime seconds)

  def appCheckUrl(myApp: DeployableApp): String = s"http://$host:${myApp.checkPort}/${myApp.checkUri}"


}
