package io.vamp.test.scenario.sava

import io.vamp.test.model.DeployableApp

object SavaExampleFixtures {

  val mySava1_0 = new DeployableApp(
    name = "sava 1.0",
    filename = "/io/vamp/test/sava/sava1.0.yml",
    nrOfServices = 1,
    checkPort = 9050,
    checkUri = "public/",
    checkResponsePart = Seq(
      "<h1 class=\"cover-heading\">Monolith 1.0</h1>"
    )
  )

  val mySavaCanary = new DeployableApp(
    name = "sava canary",
    filename = "/io/vamp/test//sava/sava-canary-start.yml",
    nrOfServices = 2,
    checkPort = 9050,
    checkUri = "public/",
    checkResponsePart = Seq(
      "<h1 class=\"cover-heading\">Monolith 1.0</h1>",
      "<h1 class=\"cover-heading\">Monolith 1.1</h1>")
  )

  val mySavaCanaryFilter = new DeployableApp(
    name = "sava canary (filter)",
    filename = "/io/vamp/test/sava/sava-canary-filter.yml",
    nrOfServices = 2,
    checkPort = 9050,
    checkUri = "public/",
    checkResponsePart = Seq(
      "<h1 class=\"cover-heading\">Monolith 1.0</h1>",
      "<h1 class=\"cover-heading\">Monolith 1.1</h1>")
  )

  val mySava1_1 = new DeployableApp(
    name = "sava 1.2",
    filename = "/io/vamp/test/sava/sava1.2.yml",
    nrOfServices = 3,
    checkPort = 9060,
    checkUri = "public/",
    checkResponsePart = Seq("<h1 class=\"cover-heading\">Frontend &amp; 2 Backends</h1>")
  )


}
