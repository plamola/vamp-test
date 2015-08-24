package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.Escalation
import io.vamp.core.model.reader.{EscalationReader, YamlReader}

class EscalationOperations extends ArtifactOperations[Escalation] {

  val endpointName = "escalations"

  def responseConverter : YamlReader[Escalation] = EscalationReader

 }





