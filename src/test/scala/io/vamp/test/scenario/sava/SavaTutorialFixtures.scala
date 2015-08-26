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
    textRequiredInResponse = "Frontend & 2 Backends")

  val savaFrontEnd1_3 = Frontend(
    name = "Sava Frontend 1.3",
    textRequiredInResponse = "Frontend & Backend")


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

  val mySava1_2 = new DeployableApp(
    name = "Sava 1.2",
    filename = "/io/vamp/test/sava/sava1.2.yml",
    nrOfServices = 3,
    endpoint = 9060,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_2)
  )

  val mySava1_2_remove = new DeployableApp(
    name = "Sava 1.2 remove",
    filename = "/io/vamp/test/sava/sava1.2-remove.yml",
    nrOfServices = 0,
    endpoint = 0,
    checkUri = "",
    frontends = Seq.empty
  )


  val mySava1_2_3_merged = new DeployableApp(
    name = "Sava 1.2 merged with 1.3",
    filename = "/io/vamp/test/sava/sava1.3.yml",
    nrOfServices = 5,
    endpoint = 9060,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_2)
  )

  val mySava1_3_ab_start = new DeployableApp(
    name = "Sava 1.3 - start ab-test",
    filename = "/io/vamp/test/sava/sava1.3-ab-start.yml",
    nrOfServices = 5,
    endpoint = 9060,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_2.copy(weight = 50), savaFrontEnd1_3.copy(weight = 50))
  )

  val mySava1_3_ab_end = new DeployableApp(
    name = "Sava 1.3 - end ab test",
    filename = "/io/vamp/test/sava/sava1.3-ab-end.yml",
    nrOfServices = 5,
    endpoint = 9060,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_3)
  )

  val mySava1_3 = new DeployableApp(
    name = "Sava 1.3",
    filename = "/io/vamp/test/sava/sava1.3.yml",
    nrOfServices = 2,
    endpoint = 9060,
    checkUri = "public/",
    frontends = Seq(savaFrontEnd1_3)
  )


}
