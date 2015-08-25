package io.vamp.test.core

import io.vamp.core.model.artifact._
import io.vamp.test.common.CleanCoreEnvironment
import io.vamp.test.core.yaml._
import org.scalatest.{BeforeAndAfterAll, FeatureSpec}


trait CleanupCoreDb extends FeatureSpec with CleanCoreEnvironment with BeforeAndAfterAll {


  override protected def beforeAll(): Unit = {
    cleanDeploymentsAndArtifacts()
    super.beforeAll()
  }


  override protected def afterAll(): Unit = {
    //cleanDeploymentsAndArtifacts()
    super.afterAll()
  }


  def cleanDeploymentsAndArtifacts() {
    removeDeployments()
    cleanupArtifact[Blueprint](new BlueprintOperations)
    cleanupArtifact[Breed](new BreedOperations)
    cleanupArtifact[Escalation](new EscalationOperations)
    cleanupArtifact[Filter](new FilterOperations)
    cleanupArtifact[Scale](new ScaleOperations)
    cleanupArtifact[Sla](new SlaOperations)

  }

  private def removeDeployments(): Unit = {
    if (getAllDeployments.nonEmpty) {
      info(s"Removing ${getAllDeployments.size} deployments")
      removeAllDeployments()
      assert(getAllDeployments.isEmpty, "Still some left")
    }
  }

  private def cleanupArtifact[T <: Artifact](bp: ArtifactOperations[T]): Unit = {
    bp.getAll.size match {
      case 0 => // Nothing to do
      case nr =>
        info(s"Removing $nr ${bp.endpointName}")
        bp.deleteAll()
        assert(bp.getAll.isEmpty, s"Still some ${bp.endpointName} left")

    }
  }


}
