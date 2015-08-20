package io.vamp.test.common

import io.vamp.core.model.artifact.Artifact

trait ArtifactTools[T <: Artifact] {

  def getAll: List[T]

  def getByName(name: String): Option[T]

  def create(artifact: T): Option[T]

  def update(name: String, artifact: T) : Option[T]

  def delete(name: String)

  def deleteAll() = getAll.foreach { artifact => delete(artifact.name) }

}

