package io.vamp.test.scenario.sava


import io.vamp.test.common.DeploymentTest
import io.vamp.test.model.Browsers

trait SavaTutorial extends DeploymentTest {

  feature("The user can run through the example on the vamp.io website") {
    info(s"I will be using the $interfaceDescription")

    // STEP 1
    scenario("Deploying your first blueprint") {
      val deploymentName = performDeployment(SavaTutorialFixtures.mySava1_0)
      validateDeployment(SavaTutorialFixtures.mySava1_0, deploymentName)
      undeployAndValidate(SavaTutorialFixtures.mySava1_0, deploymentName)
    }

    // STEP 2
    scenario("Doing a canary release") {
      val deploymentName = performDeployment(SavaTutorialFixtures.mySava1_0)
      validateDeployment(SavaTutorialFixtures.mySava1_0, deploymentName)

      performDeploymentUpdate(SavaTutorialFixtures.mySavaCanary, deploymentName)
      validateDeployment(SavaTutorialFixtures.mySavaCanary, deploymentName)

      info("[TODO] Confirm both services are reachable and load is distributed evenly (sort of)")
      //TODO Verify both services are reachable

      performDeploymentUpdate(SavaTutorialFixtures.mySavaCanaryFilter, deploymentName)
      validateDeployment(SavaTutorialFixtures.mySavaCanaryFilter, deploymentName)

      // Filter routes chrome to different front-end, so lets check if chrome goes to the new version
      checkWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_1, Browsers.Chrome)

      // All other browser should go to the old version
      checkWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_0, Browsers.Firefox)
      checkWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_0, Browsers.Safari)
      checkWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_0, Browsers.IE)

      undeployAndValidate(SavaTutorialFixtures.mySavaCanaryFilter, deploymentName)
    }

    // STEP 3
    scenario("Splitting the monolith into services") {
      val deploymentNameSava11 = performDeployment(SavaTutorialFixtures.mySava1_1)
      validateDeployment(SavaTutorialFixtures.mySava1_1, deploymentNameSava11)
      undeployAndValidate(SavaTutorialFixtures.mySava1_1, deploymentNameSava11)
    }

  }


}
