package io.vamp.test.common

import io.vamp.core.model.artifact.Artifact


trait ArtifactInterface[T <: Artifact] {

  def talkToInterface(command: String, arguments: Option[String] = None): Option[String]

  def wrapBody(artifact: T): Option[String]

  def responseToList(result: String, converter: String => T): List[T]

  def getAllCommandString: String

  def getByNameCommandString(name: String): String

  def getCreateCommandString: String

  def getUpdateCommandString(name: String): String

  def getDeleteCommandString(name: String): String

}



