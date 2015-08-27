package io.vamp.test.cli.yaml

import io.vamp.core.model.artifact.Artifact
import io.vamp.test.common.ArtifactOperations


trait ArtifactCliOperations[T <: Artifact] extends ArtifactOperations[T] with CliYamlInterface {

  def artifactType = "override me"

  def talkToInterface(command: String, arguments: Option[String] = None) = execCommand(command = command,arguments = arguments)

  def responseToList(result: String, converter: String => T): List[T] = yamlArrayListToList(result).map(single => converter(single))

  def wrapBody(artifact: T): Option[String] = Some(artifactToYaml(artifact))

  def getAllCommandString = s"list $artifactType"

  def getByNameCommandString(name: String) = s"inspect $artifactType $name"

  def getCreateCommandString = s"create $artifactType"

  def getUpdateCommandString(name: String) = s"update $artifactType $name"

  def getDeleteCommandString(name: String) = s"remove $artifactType $name"

}





