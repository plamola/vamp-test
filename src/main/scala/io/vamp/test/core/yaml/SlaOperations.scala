package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.Sla
import io.vamp.core.model.reader.{SlaReader, YamlReader}

class SlaOperations extends ArtifactOperations[Sla] {

  val endpointName = "slas"

  def responseConverter : YamlReader[Sla] = SlaReader

 }





