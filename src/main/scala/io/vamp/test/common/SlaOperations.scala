package io.vamp.test.common

import io.vamp.core.model.artifact.Sla
import io.vamp.core.model.reader.SlaReader

class SlaOperations(interface: ArtifactInterface[Sla]) extends ArtifactOperations[Sla] {

  override val artifactInterface: ArtifactInterface[Sla] = interface

  override def artifactType = "slas"

  def responseConverter(result: String) = SlaReader.read(result)
}





