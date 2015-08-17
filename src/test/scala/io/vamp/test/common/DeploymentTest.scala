package io.vamp.test.common

import io.vamp.core.model.artifact.Deployment
import io.vamp.test.model.DeployableApp
import org.scalatest.FeatureSpec
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.language.postfixOps


trait DeploymentTest extends FeatureSpec with DeploymentTools {


  def validateDeployment(myApp: DeployableApp, deploymentName: String) = {

    assert(!deploymentName.isEmpty, "has a deployment name")

    info(s"Confirm ${myApp.name} has ${myApp.nrOfServices} running service(s)")
    //val startTime = System.currentTimeMillis()
    assert(Await.result(getAndVerifyDeploymentStates(deploymentName, myApp), myApp.deploymentWaitTime seconds), "incorrect number of services are running")
    //info(s"Took ${System.currentTimeMillis() - startTime} msec")

    info(s"Confirm ${myApp.name} responds correctly on http://$host:${myApp.checkPort}/${myApp.checkUri}")
    val page = getApplicationPage(myApp)
    assert(myApp.checkResponsePart.map(checkString => page.indexOf(checkString) > 1).exists(value => true), "Could not find page content")
  }


  def undeployAndValidate(myApp: DeployableApp, deploymentName: String) = {

    //info("looking up the deployed app")
    val deploymentOption: Option[Deployment] = getDeploymentbyName(deploymentName)
    assert(deploymentOption.isDefined, "Deployment not found")

    info(s"Confirm ${myApp.name} can be undeployed")
    assert(undeploy(deploymentOption.get).isDefined, "Incorrect response from undeploy")

    info(s"Confirm ${myApp.name} has gone from vamp")
    //val startTime = System.currentTimeMillis()
    assert(Await.result(verifyDeploymentIsRemoved(deploymentName), myApp.undeployWaitTime seconds), "still there")
    //info(s"Took ${System.currentTimeMillis() - startTime} msec")

    info(s"Confirm ${myApp.name} is no longer active on http://$host:${myApp.checkPort}/${myApp.checkUri}")
    try {
      getApplicationPage(myApp)
      fail("Application still running")
    } catch {
      case e: java.util.concurrent.TimeoutException => // Timeout is acceptable
      case e: java.util.concurrent.ExecutionException => // Connection refused is also acceptable
      case e: Throwable => fail(s"Unexpected error: ${e.getMessage}")
    }
  }

}
