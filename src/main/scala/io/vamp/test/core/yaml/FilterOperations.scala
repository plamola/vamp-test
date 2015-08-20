package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.Filter
import io.vamp.core.model.reader.{FilterReader, YamlReader}

class FilterOperations extends ArtifactOperations[Filter] {

  val endpointName = "filters"

  def responseConverter : YamlReader[Filter] = FilterReader

 }





