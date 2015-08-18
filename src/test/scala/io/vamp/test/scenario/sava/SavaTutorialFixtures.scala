package io.vamp.test.scenario.sava

import io.vamp.test.model.{DeployableApp, Frontend}

/**
  * Fixures used for the Sava Tutorial test
  */
object SavaTutorialFixtures {


  val savaFrontEnd1_0 = Frontend(
    name = "Sava Frontend 1.0",
    textRequiredInResponse = "Monolith 1.0")

  val savaFrontEnd1_1 = Frontend(
    name = "Sava Frontend 1.1",
    textRequiredInResponse = "Monolith 1.1")


  val savaFrontEnd1_2 = Frontend(
    name = "Sava Frontend 1.2",
    textRequiredInResponse = "Frontend &amp; 2 Backends")


  val mySava1_0 = new DeployableApp(
    name = "Sava 1.0",
    filename = "/io/vamp/test/sava/sava1.0.yml",
    nrOfServices = 1,
    endpoint = 9050,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_0)
  )

  val mySavaCanary = new DeployableApp(
    name = "Sava canary",
    filename = "/io/vamp/test/sava/sava-canary-start.yml",
    nrOfServices = 2,
    endpoint = 9050,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_0.copy(weight = 50), savaFrontEnd1_1.copy(weight = 50))
  )

  val mySavaCanaryFilter = new DeployableApp(
    name = "Sava canary with filter",
    filename = "/io/vamp/test/sava/sava-canary-filter.yml",
    nrOfServices = 2,
    endpoint = 9050,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_0.copy(weight = 100), savaFrontEnd1_1.copy(weight = 0))
  )

  val mySava1_1 = new DeployableApp(
    name = "Sava 1.2",
    filename = "/io/vamp/test/sava/sava1.2.yml",
    nrOfServices = 3,
    endpoint = 9060,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_2)
  )


}
