package io.vamp.test.model


object AppDefinitions {

  val mySava1_0 = new DeployableApp(
    name = "sava 1.0",
    filename = "sava1.0.yml",
    nrOfServices = 1,
    checkPort = 9050,
    checkUri = "public/",
    checkResponsePart = Seq(
      "<h1 class=\"cover-heading\">Monolith 1.0</h1>"
    )
  )

  val mySavaCanary = new DeployableApp(
    name = "sava canary",
    filename = "sava-canary-start.yml",
    nrOfServices = 2,
    checkPort = 9050,
    checkUri = "public/",
    checkResponsePart = Seq(
      "<h1 class=\"cover-heading\">Monolith 1.0</h1>",
      "<h1 class=\"cover-heading\">Monolith 1.1</h1>")
  )

  val mySavaCanaryFilter = new DeployableApp(
    name = "sava canary (filter)",
    filename = "sava-canary-filter.yml",
    nrOfServices = 2,
    checkPort = 9050,
    checkUri = "public/",
    checkResponsePart = Seq(
      "<h1 class=\"cover-heading\">Monolith 1.0</h1>",
      "<h1 class=\"cover-heading\">Monolith 1.1</h1>")
  )

  val mySava1_1 = new DeployableApp(
    name = "sava 1.2",
    filename = "sava1.2.yml",
    nrOfServices = 3,
    checkPort = 9060,
    checkUri = "public/",
    checkResponsePart = Seq("<h1 class=\"cover-heading\">Frontend &amp; 2 Backends</h1>")
  )


}
