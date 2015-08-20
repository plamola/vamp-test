package io.vamp.test.core.yaml


import io.vamp.core.model.artifact.Blueprint
import io.vamp.core.model.reader.{YamlReader, BlueprintReader}


class BlueprintOperations extends ArtifactOperations[Blueprint] {

  val endpointName = "blueprints"

  def responseConverter : YamlReader[Blueprint] = BlueprintReader


 }





