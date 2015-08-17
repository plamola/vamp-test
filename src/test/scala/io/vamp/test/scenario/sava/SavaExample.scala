package io.vamp.test.scenario.sava

import io.vamp.core.model.reader.BlueprintReader
import io.vamp.test.common.DeploymentTest
import SavaExampleFixtures
import traits.FileAccess


trait SavaExample extends DeploymentTest with FileAccess{

  feature("The user can run through the example on the vamp.io website") {
    info(s"I will be using the $interfaceDescription")

    // STEP 1
    scenario("Deploying your first blueprint") {
      val deploymentName = deploy(BlueprintReader.read(readFile(SavaExampleFixtures.mySava1_0.filename))).get.name
      validateDeployment(SavaExampleFixtures.mySava1_0, deploymentName)
      undeployAndValidate(SavaExampleFixtures.mySava1_0, deploymentName)
    }

    // STEP 2
    scenario("Doing a canary release") {
      val deploymentName = deploy(BlueprintReader.read(readFile(SavaExampleFixtures.mySava1_0.filename))).get.name
      validateDeployment(SavaExampleFixtures.mySava1_0, deploymentName)

      info("Update to a canary deployment")
      deploymentUpdate(BlueprintReader.read(readFile(SavaExampleFixtures.mySavaCanary.filename)), deploymentName)
      validateDeployment(SavaExampleFixtures.mySavaCanary, deploymentName)

      info("[TODO] Confirm both services are reachable")
      //TODO Verify both services are reachable

      info("Update the canary deployment with a filter")
      deploymentUpdate(BlueprintReader.read(readFile(SavaExampleFixtures.mySavaCanaryFilter.filename)), deploymentName)
      validateDeployment(SavaExampleFixtures.mySavaCanaryFilter, deploymentName)

      info("[TODO] Confirm the filter works, by switching between different user agents")
      //TODO verify the filter works, by switching between different user agents

      undeployAndValidate(SavaExampleFixtures.mySavaCanaryFilter, deploymentName)
    }

    // STEP 3
    scenario("Splitting the monolith into services"){
      val deploymentNameSava11 = deploy(BlueprintReader.read(readFile(SavaExampleFixtures.mySava1_1.filename))).get.name
      validateDeployment(SavaExampleFixtures.mySava1_1, deploymentNameSava11)
      undeployAndValidate(SavaExampleFixtures.mySava1_1, deploymentNameSava11)
    }

  }


}
