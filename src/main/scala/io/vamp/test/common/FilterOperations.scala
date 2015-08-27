package io.vamp.test.common

import io.vamp.core.model.artifact.Filter
import io.vamp.core.model.reader.FilterReader

class FilterOperations(interface: ArtifactInterface[Filter]) extends ArtifactOperations[Filter] {

  override val artifactInterface: ArtifactInterface[Filter] = interface

  override def artifactType = "filters"

  def responseConverter(result: String) = FilterReader.read(result)

}





