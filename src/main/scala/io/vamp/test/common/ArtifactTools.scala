package io.vamp.test.common

import io.vamp.core.model.artifact.Artifact


trait ArtifactTools {

  def getAll[A <: Artifact]: List[A]

  def getByName[A <: Artifact]: A

  def create[A <: Artifact](artifact: A)

  def update[A <: Artifact](artifact: A)

  def delete[A <: Artifact](artifact: A)

  def deleteAll[A <: Artifact]() {
    getAll[A].foreach { artifact => delete(artifact) }
  }

}

