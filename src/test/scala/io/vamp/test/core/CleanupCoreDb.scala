package io.vamp.test.core

import io.vamp.core.model.artifact._
import io.vamp.test.common._
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
    //TODO extract the ArtifactYamlInterface
    cleanupArtifact[Blueprint](new BlueprintOperations(new ArtifactYamlInterface[Blueprint]("blueprints")))
    cleanupArtifact[Breed](new BreedOperations(new ArtifactYamlInterface[Breed]("breeds")))
    cleanupArtifact[Escalation](new EscalationOperations(new ArtifactYamlInterface[Escalation]("escalations")))
    cleanupArtifact[Filter](new FilterOperations(new ArtifactYamlInterface[Filter]("filters")))
    cleanupArtifact[Scale](new ScaleOperations(new ArtifactYamlInterface[Scale]("scales")))
    cleanupArtifact[Sla](new SlaOperations(new ArtifactYamlInterface[Sla]("slas")))
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
        info(s"- Removing $nr ${artifactOperations.artifactType}")
        artifactOperations.deleteAll()
        assert(artifactOperations.getAll.isEmpty, s"Still some ${artifactOperations.artifactType} left")

    }
  }


}
