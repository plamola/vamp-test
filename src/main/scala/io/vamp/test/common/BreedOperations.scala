package io.vamp.test.common

import io.vamp.core.model.artifact.Breed
import io.vamp.core.model.reader.BreedReader

class BreedOperations(interface: ArtifactInterface[Breed]) extends ArtifactOperations[Breed] {

  override val artifactInterface: ArtifactInterface[Breed] = interface

  override def artifactType = "breeds"

  def responseConverter(result: String) = BreedReader.read(result)

}





