package io.vamp.test.common

import io.vamp.core.model.artifact.Artifact

trait CleanCoreEnvironment extends DeploymentTools {


  def hasDeployments: Boolean = getAllDeployments.isEmpty


  def removeAllArtifacts()

  /**
   * Returns a list of all artifacts
   * Deployments are excluded from the list
   * @return
   */
  def getAllArtifacts: List[Artifact]

  def hasArtifacts: Boolean = getAllArtifacts.isEmpty

  def removeAllBlueprints(): Unit = {

  }


}

