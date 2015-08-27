package io.vamp.test.common

import io.vamp.core.model.artifact.Artifact


trait ArtifactOperations[T <: Artifact] {

  def artifactType: String

  def responseConverter(result: String): T

  val artifactInterface: ArtifactInterface[T]


  def getAll: List[T] = artifactInterface.talkToInterface(command = artifactInterface.getAllCommandString) match {
    case Some(result) => artifactInterface.responseToList(result, responseConverter)
    case None => List.empty
  }

  def getByName(name: String): Option[T] = artifactInterface.talkToInterface(command = artifactInterface.getByNameCommandString(name)) match {
    case Some(result) => Some(responseConverter(result))
    case None => None
  }

  def create(artifact: T): Option[T] = artifactInterface.talkToInterface(command = artifactInterface.getCreateCommandString, arguments = artifactInterface.wrapBody(artifact)) match {
    case Some(result) => Some(responseConverter(result))
    case None => None
  }

  def update(name: String, artifact: T): Option[T] = artifactInterface.talkToInterface(command = artifactInterface.getUpdateCommandString(name), arguments = artifactInterface.wrapBody(artifact)) match {
    case Some(result) => Some(responseConverter(result))
    case None => None
  }

  def delete(name: String) = artifactInterface.talkToInterface(command = artifactInterface.getDeleteCommandString(name))

  def deleteAll() = getAll.foreach { artifact => delete(artifact.name) }

}





