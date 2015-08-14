package io.vamp.test.core.api


import io.vamp.core.model.artifact.Deployment
import io.vamp.core.model.reader.BlueprintReader
import io.vamp.test.model.{AppDefinitions, DeployableApp}
import io.vamp.test.pulse.CleanableTest
import org.scalatest._
import traits.FileAccess

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps


class DeploymentSuite extends FlatSpec with Matchers with FileAccess with Retries with DeploymentTools {


  info("Start with a single sava deployment")
  var deploymentName = deployViaRestAPI(BlueprintReader.read(readFile(AppDefinitions.mySava1_0.filename))).get.name
  validateDeployment(AppDefinitions.mySava1_0, deploymentName)


  info("Update to a canary deployment")
  deploymentUpdateViaRestAPI(BlueprintReader.read(readFile(AppDefinitions.mySavaCanary.filename)), deploymentName)
  validateDeployment(AppDefinitions.mySavaCanary, deploymentName)

  it must "verify both services are reachable" is (pending)


  info("Update the canary deployment with a filter")
  deploymentUpdateViaRestAPI(BlueprintReader.read(readFile(AppDefinitions.mySavaCanaryFilter.filename)), deploymentName)
  validateDeployment(AppDefinitions.mySavaCanaryFilter, deploymentName)


  it must "verify the filter works, by switching between different user agents" is (pending)


  info("Deploy sava 1.2")
  var deploymentNameSava11 = deployViaRestAPI(BlueprintReader.read(readFile(AppDefinitions.mySava1_1.filename))).get.name
  validateDeployment(AppDefinitions.mySava1_1, deploymentNameSava11)
  undeployAndValidate(AppDefinitions.mySava1_1, deploymentNameSava11)


  // clean up sava 1.0 canary
  undeployAndValidate(AppDefinitions.mySavaCanaryFilter, deploymentName)



  def validateDeployment(myApp: DeployableApp, deploymentName: String) = {
    it should s"verify ${myApp.name} is running" in {
      deploymentName.isEmpty shouldBe (false)
    }

    it should s"verify ${myApp.name} has ${myApp.nrOfServices} running service(s)" in {
      val startTime = System.currentTimeMillis()
      Await.result(getAndVerifyDeploymentStates(deploymentName, myApp), myApp.deploymentWaitTime seconds) should be(true)
      info(s"Took ${(System.currentTimeMillis() - startTime)} msec")
    }

    it should s"verify ${myApp.name} responds correctly on http://$host:${myApp.checkPort}/${myApp.checkUri}" in {
      val page = getApplicationPage(myApp)
      assert(myApp.checkResponsePart.map(checkString => page.indexOf(checkString) > 1).exists(value => true))
    }
  }


  def undeployAndValidate(myApp: DeployableApp, deploymentName: String) = {

    val deploymentOption: Option[Deployment] = getDeploymentbyNameViaRestAPI(deploymentName)

    it should s"verify ${myApp.name} can be undeployed" taggedAs CleanableTest in {
      deploymentOption shouldNot be(None)
      undeployViaRestAPI(deploymentOption.get).get shouldBe an[Deployment]
    }


    it should s"verify ${myApp.name} has gone from vamp" in {
      val startTime = System.currentTimeMillis()
      Await.result(verifyDeploymentIsRemoved(deploymentName), myApp.undeployWaitTime seconds) should be(true)
      info(s"Took ${(System.currentTimeMillis() - startTime)} msec")
    }

    it should s"verify ${myApp.name} is no longer active on http://$host:${myApp.checkPort}/${myApp.checkUri}" in {
      intercept[java.util.concurrent.TimeoutException] {
        getApplicationPage(myApp)
      }
    }


  }


}

