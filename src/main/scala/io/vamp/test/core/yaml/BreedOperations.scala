package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.Breed
import io.vamp.core.model.reader.{BreedReader, YamlReader}

class BreedOperations extends ArtifactOperations[Breed] {

  val endpointName = "breeds"

  def responseConverter : YamlReader[Breed] = BreedReader

 }





