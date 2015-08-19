package io.vamp.test.core

import io.vamp.core.model.artifact.Artifact
import io.vamp.test.common.CleanCoreEnvironment


trait CleanCoreEnvironmentRestAPI extends  DeploymentToolsRestApi with CleanCoreEnvironment {



  override def removeAllArtifacts(): Unit = {
    //TODO implement this
  }

  override def getAllArtifacts : List[Artifact] = {
    //TODO implement this
    List.empty
  }



}
