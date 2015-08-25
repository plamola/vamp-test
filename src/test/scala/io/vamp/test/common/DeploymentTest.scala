package io.vamp.test.common

import io.vamp.core.model.artifact.Deployment
import io.vamp.core.model.reader.BlueprintReader
import io.vamp.test.model.{BrowserDefinition, DeployableApp, Frontend}
import org.scalatest.Matchers

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps


trait DeploymentTest extends VampTest with DeploymentTools with Matchers {

  /** *
    * Deploy a blueprint
    * @param myApp - Application description
    * @return name of the deployment
    */
  def performDeployment(myApp: DeployableApp): String = {
    info(s"Deploying [${myApp.name}]")
    deploy(BlueprintReader.read(readFile(myApp.filename))).get.name
  }

  /** *
    * Update an existing deployment
    * @param myApp - Application description
    * @param deploymentName - Name of the existing deployment
    * @return
    */
  def performDeploymentUpdate(myApp: DeployableApp, deploymentName: String): Option[Deployment] = {
    info(s"Updating deployment to [${myApp.name}]")
    deploymentUpdate(BlueprintReader.read(readFile(myApp.filename)), deploymentName)
  }

  /** *
    * Check if all the services are running & the application responds at the assigned endpoint
    * @param myApp - Application description
    * @param deploymentName - Name of the existing deployment
    */
  def validateDeployment(myApp: DeployableApp, deploymentName: String) = {
    assert(!deploymentName.isEmpty, "Does not have a valid deployment name")

    info(s"Confirm [${myApp.name}] has ${myApp.nrOfServices} running service(s)")
    assert(Await.result(getAndVerifyDeploymentStates(deploymentName, myApp), myApp.deploymentWaitTime seconds), "incorrect number of services are running")
    //TODO fix this
    Thread.sleep(2000)
    assert(Await.result(doesApplicationRespond(myApp), myApp.deploymentWaitTime seconds), s"No valid response from application within ${myApp.deploymentWaitTime} seconds")
  }


  /** *
    * try to get a valid response from the application
    * @param myApp - Application description
    * @return
    */
  def doesApplicationRespond(myApp: DeployableApp): Future[Boolean] = Future {
    Poll {
      try {
        getApplicationPage(myApp)
      }
      catch {
        case e: Throwable => ""
      }
    } until (page => myApp.frontends.map(frontend => page.indexOf(frontend.textRequiredInResponse) > 1).exists(value => true))
    true
  }


  /** *
    * Undeploy an deployment and verify it is no longer answering at the endpoint
    * @param myApp  - Application description
    * @param deploymentName  - Name of the existing deployment
    */
  def undeployAndValidate(myApp: DeployableApp, deploymentName: String) = {
    val deploymentOption: Option[Deployment] = getDeploymentbyName(deploymentName)
    assert(deploymentOption.isDefined, "Deployment not found")

    info(s"Confirm [${myApp.name}] can be undeployed")
    assert(undeploy(deploymentOption.get).isDefined, "Incorrect response from undeploy")

    info(s"Confirm [${myApp.name}] has been removed from vamp")
    assert(Await.result(verifyDeploymentIsRemoved(deploymentName), myApp.undeployWaitTime seconds), "still there")
    info(s"Confirm [${myApp.name}] is no longer active on http://${appCheckUrl(myApp)}")
    try {
      getApplicationPage(myApp)
      fail(s"Application [${myApp.name}] still running")
    } catch {
      case e: java.util.concurrent.TimeoutException => // Timeout is acceptable
      case e: java.util.concurrent.ExecutionException => // Connection refused is also acceptable
      case e: Throwable => fail(s"Unexpected error: ${e.getMessage}")
    }
  }

  /** *
    * Check to see if any valid response is received at the endpoint
    * @param myApp  - Application description
    */
  def testWebResponseAny(myApp: DeployableApp): Unit = {
    info(s"Confirm [${myApp.name}] responds correctly on ${appCheckUrl(myApp)}")
    val page = getApplicationPage(myApp)
    assert(myApp.frontends.map(frontend => page.indexOf(frontend.textRequiredInResponse) > 1).exists(value => true), "Incorrect page content")
  }

  /** *
    * Check to see if a specific endpoint is answering (browser specific)
    * @param myApp    - Application description
    * @param frontEnd - Frontend expected to respond
    * @param browser  - Browser being used in the test (user-agent)
    */
  def testWebResponseSpecific(myApp: DeployableApp, frontEnd: Frontend, browser: BrowserDefinition): Unit = {
    info(s"Confirm [${myApp.name}] responds correctly on ${appCheckUrl(myApp)} to ${browser.name}")
    val page = getApplicationPage(myApp, browser.headers)
    assert(page.indexOf(frontEnd.textRequiredInResponse) > 1, s"Incorrect page content. [$page] does not contain [${frontEnd.textRequiredInResponse}]")
  }


  case class ResponseCount(frontEnd: Frontend, replies: Int = 0, percentageOfTraffic: Int)

  /** *
    * Test if the weight (load) is distributed correctly over the front ends.
    * @param myApp               - Description of the app
    * @param nrOfRequests        - Number of requests to fire at the application
    * @param maxDeviationPct     - Maximum deviation of the assigned weight (percentage)
    * @param headers             - Optional headers to included, for example cookies or user-agent
    */
  def testWeightDistribution(myApp: DeployableApp, nrOfRequests: Int, maxDeviationPct: Int, headers: List[(String, String)] = List.empty): Unit = {
    info(s"Testing weight distribution, doing a $nrOfRequests requests, accepting a deviation of $maxDeviationPct%")
    var identifiedResponses: List[Frontend] = List.empty
    1 to nrOfRequests foreach { n =>
      try {
        val page = getApplicationPage(myApp, headers)
        myApp.frontends.foreach { frontEnd =>
          if (page.indexOf(frontEnd.textRequiredInResponse) > 1) identifiedResponses = identifiedResponses ++ List(frontEnd)
        }
      } catch {
        case e: Throwable => /*ignoring for now */
      }
    }

    // Make sure all requests have been identified to originate from a valid frontend
    assert(identifiedResponses.size == nrOfRequests, s"Reason: Could not uniquely map each request to a frontend. Requests:  $nrOfRequests / identified: ${identifiedResponses.size}")

    // Calculate totals & percentage for each front end
    val responses: Seq[ResponseCount] = for {
      frontEnd <- myApp.frontends
      replies = identifiedResponses.count(response => frontEnd.equals(response))
    } yield ResponseCount(frontEnd = frontEnd, replies = replies, percentageOfTraffic = (replies * 100) / nrOfRequests)

    responses.foreach(response => info(s"- ${response.frontEnd.name} got ${response.replies} requests [${response.percentageOfTraffic}%]"))

    // Check if percentages are within the acceptable deviation
    responses.foreach { response =>
      val deviation = response.frontEnd.weight * maxDeviationPct / 100
      val lower = response.frontEnd.weight - deviation
      val upper = response.frontEnd.weight + deviation
      assert((response.percentageOfTraffic >= lower) && (response.percentageOfTraffic <= upper), s"${response.frontEnd.name} got ${response.percentageOfTraffic}% of the traffic, which is not in the range of $lower - $upper%")
    }

  }


}
