package io.vamp.test.core

import org.scalatest.FeatureSpec


class CleanupSuite extends FeatureSpec with CleanCoreEnvironmentRestAPI {

  feature("We can cleanup the environment") {
    info(s"I will be using the $interfaceDescription")

    // Deployments
    scenario("Removing all deployments") {

      info(s"There are ${getAllDeployments.size} deployments to remove")

      removeAllDeployments()
      assert(getAllDeployments.size == 0 , "Still some deployments left")
    }
  }


}
