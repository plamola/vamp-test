package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.Scale
import io.vamp.core.model.reader.{ScaleReader, YamlReader}

class ScaleOperations extends ArtifactOperations[Scale] {

  val endpointName = "scales"

  def responseConverter : YamlReader[Scale] = ScaleReader

 }





