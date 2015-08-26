package io.vamp.test.core

import io.vamp.core.model.artifact._
import io.vamp.test.common.CleanCoreEnvironment
import io.vamp.test.core.yaml._
import org.scalatest.{BeforeAndAfterAll, FeatureSpec}


trait CleanupCoreDb extends FeatureSpec with CleanCoreEnvironment with BeforeAndAfterAll {


  /** *
    * Executed before all tests that extend this trait
    */
  override protected def beforeAll(): Unit = {
    info("Pre test cleanup")
    cleanDeploymentsAndArtifacts()
    super.beforeAll()
  }

  /** *
    * Executed after all tests that extend this trait
    */
  override protected def afterAll(): Unit = {
    // Cleanup of artifacts not done after testing, since it won't allow for inspection of artifacts of failed tests
    super.afterAll()
  }

  /** *
    * Remove all static artifacts
    */
  def cleanDeploymentsAndArtifacts() {
    removeDeployments()
    cleanupArtifact[Blueprint](new BlueprintOperations)
    cleanupArtifact[Breed](new BreedOperations)
    cleanupArtifact[Escalation](new EscalationOperations)
    cleanupArtifact[Filter](new FilterOperations)
    cleanupArtifact[Scale](new ScaleOperations)
    cleanupArtifact[Sla](new SlaOperations)
  }

  /** *
    * Remove all dynamic artifacts (deployments)
    */
  private def removeDeployments(): Unit = {
    if (getAllDeployments.nonEmpty) {
      info(s"- Removing ${getAllDeployments.size} deployments")
      removeAllDeployments()
      assert(getAllDeployments.isEmpty, "Still some left")
    }
  }

  /** *
    * Remove all artifacts of specified type
    * @param artifactOperations - Artifact operations handler
    * @tparam T       - Type of the artifact
    */
  private def cleanupArtifact[T <: Artifact](artifactOperations: ArtifactOperations[T]): Unit = {
    artifactOperations.getAll.size match {
      case 0 => // Nothing to do
      case nr =>
        info(s"- Removing $nr ${artifactOperations.endpointName}")
        artifactOperations.deleteAll()
        assert(artifactOperations.getAll.isEmpty, s"Still some ${artifactOperations.endpointName} left")

    }
  }


}
