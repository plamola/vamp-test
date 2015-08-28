package io.vamp.test.scenario.generic

import io.vamp.test.common.{ ConfigProvider, VampTest, VampInterface }

trait GenericTests extends VampTest with ConfigProvider with VampInterface {

  feature("We can verify some facts about the environment") {

    scenario(s"Checking if component $interfaceDescription is of version [$version]") {
      assert(version == readVersionFromInterface, "Incorrect component version")
    }

  }

}
