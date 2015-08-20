package io.vamp.test.common

import io.vamp.common.http.RestClient
import io.vamp.core.model.artifact.{Blueprint, Deployment, DeploymentService}
import io.vamp.test.model.DeployableApp
import traits.ConfigProvider

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps
import scala.concurrent.duration._


trait DeploymentTools extends ConfigProvider {

  implicit def executionContext: ExecutionContext = {
    global
  }

  val host: String = config.getString("endpoints.router.host")

  def getDeploymentbyName(name: String): Option[Deployment]

  def getAllDeployments : List[Deployment]

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
    ), myApp.applicationTimeout seconds)

  def appCheckUrl(myApp: DeployableApp): String = s"http://$host:${myApp.endpoint}/${myApp.checkUri}"

  def removeAllDeployments(maxWaitTime: Int = 60) {
    getAllDeployments.foreach { deployment =>
      undeploy(deployment)
      // Assume every deployment can be removed within 60 seconds
      assert(Await.result(verifyDeploymentIsRemoved(deployment.name), maxWaitTime seconds), s"Deployment ${deployment.name} could not be removed within reasonable time")
    }
  }


}
