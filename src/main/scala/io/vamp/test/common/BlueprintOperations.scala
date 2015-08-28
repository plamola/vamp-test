package io.vamp.test.common

import io.vamp.core.model.artifact.Blueprint
import io.vamp.core.model.reader.BlueprintReader

import scala.language.reflectiveCalls

class BlueprintOperations(interface: ArtifactInterface[Blueprint]) extends ArtifactOperations[Blueprint] {

  override val artifactInterface: ArtifactInterface[Blueprint] = interface

  override def artifactType = "blueprints"

  def responseConverter(result: String) = BlueprintReader.read(result)

}

