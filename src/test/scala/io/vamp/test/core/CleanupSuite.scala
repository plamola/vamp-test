package io.vamp.test.core

import io.vamp.core.model.artifact.{Filter, Artifact, Blueprint, Breed}
import io.vamp.test.core.yaml._
import org.scalatest.FeatureSpec


class CleanupSuite extends FeatureSpec with CleanCoreEnvironmentRestAPI {

  feature("We can cleanup the environment") {
    info(s"I will be using the $interfaceDescription")

    // Deployments
    scenario("Removing all deployments") {
      info(s"Will remove  ${getAllDeployments.size} deployments")
      removeAllDeployments()
      assert(getAllDeployments.isEmpty, "Still some left")
    }

    cleanupArtifact[Blueprint](new BlueprintOperations)
    cleanupArtifact[Breed](new BreedOperations)
    cleanupArtifact[Filter](new FilterOperations)
    //TODO Add other artifacts

  }


  def cleanupArtifact[T <: Artifact](bp: ArtifactOperations[T]): Unit = {
    scenario(s"Removing all ${bp.endpointName}") {
      bp.getAll.size match {
        case 0 => info("Nothing to remove")

        case nr =>
          info(s"REmoving $nr ${bp.endpointName}")
          bp.deleteAll()
          assert(bp.getAll.isEmpty, s"Still some ${bp.endpointName} left")
      }
    }

  }


}
