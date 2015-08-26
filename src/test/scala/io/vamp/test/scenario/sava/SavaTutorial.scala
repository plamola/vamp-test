package io.vamp.test.scenario.sava


import io.vamp.test.common.{DeploymentTest, VampInterface}
import io.vamp.test.core.CleanupCoreDb
import io.vamp.test.model.Browsers

trait SavaTutorial extends DeploymentTest with VampInterface with CleanupCoreDb {

  feature("The user can run through the example on the vamp.io website") {
    info(s"I will be using the $interfaceDescription")

    // STEP 1
    scenario("1. Deploying your first blueprint") {
      val deploymentName = performDeployment(SavaTutorialFixtures.mySava1_0)
      validateDeployment(SavaTutorialFixtures.mySava1_0, deploymentName)
      undeployAndValidate(SavaTutorialFixtures.mySava1_0, deploymentName)
    }

    // STEP 2
    scenario("2. Doing a canary release") {
      val deploymentName = performDeployment(SavaTutorialFixtures.mySava1_0)
      validateDeployment(SavaTutorialFixtures.mySava1_0, deploymentName)

      performDeploymentUpdate(SavaTutorialFixtures.mySavaCanary, deploymentName)
      validateDeployment(SavaTutorialFixtures.mySavaCanary, deploymentName)

      // Make sure the weight is distributed evenly
      testWeightDistribution(SavaTutorialFixtures.mySavaCanary, 100, 10)

      performDeploymentUpdate(SavaTutorialFixtures.mySavaCanaryFilter, deploymentName)
      validateDeployment(SavaTutorialFixtures.mySavaCanaryFilter, deploymentName)

      // The filter should route Chrome to the new Sava 1.1 (the canary)
      testWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_1, Browsers.Chrome)

      // All other browsers should go to the old version
      testWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_0, Browsers.Firefox)
      testWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_0, Browsers.Safari)
      testWebResponseSpecific(SavaTutorialFixtures.mySavaCanaryFilter, SavaTutorialFixtures.savaFrontEnd1_0, Browsers.IE)

      undeployAndValidate(SavaTutorialFixtures.mySavaCanaryFilter, deploymentName)
    }

    // STEP 3
    scenario("3. Splitting the monolith into services") {
      val deploymentNameSava12 = performDeployment(SavaTutorialFixtures.mySava1_2)
      validateDeployment(SavaTutorialFixtures.mySava1_2, deploymentNameSava12)
      undeployAndValidate(SavaTutorialFixtures.mySava1_2, deploymentNameSava12)
    }


    // STEP 4
    scenario("4. Merging a changed topology") {
      val deploymentNameSava13 = performDeployment(SavaTutorialFixtures.mySava1_2)
      validateDeployment(SavaTutorialFixtures.mySava1_2, deploymentNameSava13)

      performDeploymentUpdate(SavaTutorialFixtures.mySava1_2_3_merged, deploymentNameSava13)
      validateDeployment(SavaTutorialFixtures.mySava1_2_3_merged, deploymentNameSava13)

      // start ab test
      performDeploymentUpdate(SavaTutorialFixtures.mySava1_3_ab_start, deploymentNameSava13)
      validateDeployment(SavaTutorialFixtures.mySava1_3_ab_start, deploymentNameSava13)

      // Make sure the weight is distributed evenly
      testWeightDistribution(SavaTutorialFixtures.mySava1_3_ab_start, 100, 10)

      // move all traffic to 1.3
      performDeploymentUpdate(SavaTutorialFixtures.mySava1_3_ab_end, deploymentNameSava13)
      validateDeployment(SavaTutorialFixtures.mySava1_3_ab_end, deploymentNameSava13)

      // remove 1.2 services
      performDeploymentDelete(SavaTutorialFixtures.mySava1_2_remove, deploymentNameSava13)

      //Check if 1.3 is still running
      validateDeployment(SavaTutorialFixtures.mySava1_3, deploymentNameSava13)

      // end of demo
      undeployAndValidate(SavaTutorialFixtures.mySava1_3, deploymentNameSava13)
    }

  }


}
