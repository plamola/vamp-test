package io.vamp.test.common

import io.vamp.core.model.artifact.Escalation
import io.vamp.core.model.reader.EscalationReader

class EscalationOperations(interface: ArtifactInterface[Escalation]) extends ArtifactOperations[Escalation] {

  override val artifactInterface: ArtifactInterface[Escalation] = interface

  override def artifactType = "escalations"

  def responseConverter(result: String) = EscalationReader.read(result)

}





