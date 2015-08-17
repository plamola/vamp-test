package io.vamp.test.scenario.sava

import io.vamp.test.model.{DeployableApp, FrontEnd}

object SavaTutorialFixtures {


  val savaFrontEnd1_0 = FrontEnd(
    checkResponsePart = "Monolith 1.0",
    appTitle = "Sava")

  val savaFrontEnd1_1 = FrontEnd(
    checkResponsePart = "Monolith 1.1",
    appTitle = "Sava")


  val savaFrontEnd1_2 = FrontEnd(
    checkResponsePart = "Frontend &amp; 2 Backends",
    appTitle = "Sava")


  val mySava1_0 = new DeployableApp(
    name = "Sava 1.0",
    filename = "/io/vamp/test/sava/sava1.0.yml",
    nrOfServices = 1,
    checkPort = 9050,
    checkUri = "public/",
    frontEnds = Seq(savaFrontEnd1_0)
  )

  val mySavaCanary = new DeployableApp(
    name = "Sava canary",
    filename = "/io/vamp/test/sava/sava-canary-start.yml",
    nrOfServices = 2,
    checkPort = 9050,
    checkUri = "public/",
    frontEnds = Seq(savaFrontEnd1_0, savaFrontEnd1_1)
  )

  val mySavaCanaryFilter = new DeployableApp(
    name = "Sava canary with filter",
    filename = "/io/vamp/test/sava/sava-canary-filter.yml",
    nrOfServices = 2,
    checkPort = 9050,
    checkUri = "public/",
    frontEnds = Seq(savaFrontEnd1_0, savaFrontEnd1_1)
  )

  val mySava1_1 = new DeployableApp(
    name = "Sava 1.2",
    filename = "/io/vamp/test/sava/sava1.2.yml",
    nrOfServices = 3,
    checkPort = 9060,
    checkUri = "public/",
    frontEnds = Seq(savaFrontEnd1_2)
  )


}
