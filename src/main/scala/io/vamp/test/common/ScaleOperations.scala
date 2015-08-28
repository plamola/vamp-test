package io.vamp.test.common

import io.vamp.core.model.artifact.Scale
import io.vamp.core.model.reader.ScaleReader

class ScaleOperations(interface: ArtifactInterface[Scale]) extends ArtifactOperations[Scale] {

  override val artifactInterface: ArtifactInterface[Scale] = interface

  override def artifactType = "scales"

  def responseConverter(result: String) = ScaleReader.read(result)

}

