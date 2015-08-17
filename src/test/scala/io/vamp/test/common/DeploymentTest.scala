package io.vamp.test.common

import io.vamp.core.model.artifact.Deployment
import io.vamp.core.model.reader.BlueprintReader
import io.vamp.test.model.{BrowserDefinition, DeployableApp, FrontEnd}
import org.scalatest.Matchers

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


trait DeploymentTest extends VampTest with DeploymentTools with Matchers {

  def performDeployment(myApp: DeployableApp): String = {
    info(s"Deploying ${myApp.name}")
    deploy(BlueprintReader.read(readFile(myApp.filename))).get.name
  }


  def performDeploymentUpdate(myApp: DeployableApp, deploymentName: String) = {
    info(s"Updating deployment to [${myApp.name}]")
    deploymentUpdate(BlueprintReader.read(readFile(myApp.filename)), deploymentName)
  }

  def validateDeployment(myApp: DeployableApp, deploymentName: String) = {

    assert(!deploymentName.isEmpty, "has a deployment name")

    info(s"Confirm [${myApp.name}] has ${myApp.nrOfServices} running service(s)")
    //val startTime = System.currentTimeMillis()
    assert(Await.result(getAndVerifyDeploymentStates(deploymentName, myApp), myApp.deploymentWaitTime seconds), "incorrect number of services are running")
    //info(s"Took ${System.currentTimeMillis() - startTime} msec")

    checkWebResponseAny(myApp)
  }


  def checkWebResponseAny(myApp: DeployableApp): Unit = {
    info(s"Confirm [${myApp.name}] responds correctly on ${appCheckUrl(myApp)}")
    val page = getApplicationPage(myApp)
    assert(myApp.frontEnds.map(frontend => page.indexOf(frontend.checkResponsePart) > 1).exists(value => true), "Incorrect page content")
  }

  def checkWebResponseSpecific(myApp: DeployableApp, frontEnd: FrontEnd, browser: BrowserDefinition): Unit = {
    info(s"Confirm [${myApp.name}] responds correctly on ${appCheckUrl(myApp)} to ${browser.name}")
    val page = getApplicationPage(myApp, browser.headers)
    assert(page.indexOf(frontEnd.checkResponsePart) > 1, s"Incorrect page content. [$page] does not contain [${frontEnd.checkResponsePart}]")
  }


  def undeployAndValidate(myApp: DeployableApp, deploymentName: String) = {

    //info("looking up the deployed app")
    val deploymentOption: Option[Deployment] = getDeploymentbyName(deploymentName)
    assert(deploymentOption.isDefined, "Deployment not found")

    info(s"Confirm [${myApp.name}] can be undeployed")
    assert(undeploy(deploymentOption.get).isDefined, "Incorrect response from undeploy")

    info(s"Confirm [${myApp.name}] has been removed from vamp")
    //val startTime = System.currentTimeMillis()
    assert(Await.result(verifyDeploymentIsRemoved(deploymentName), myApp.undeployWaitTime seconds), "still there")
    //info(s"Took ${System.currentTimeMillis() - startTime} msec")

    info(s"Confirm [${myApp.name}] is no longer active on http://${appCheckUrl(myApp)}")
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
