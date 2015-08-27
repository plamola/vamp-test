package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.Artifact
import io.vamp.test.common.ArtifactInterface


class ArtifactYamlInterface[T <: Artifact](artifact: String) extends ArtifactInterface[T] with CoreYamlInterface {

  def artifactType = artifact

  private def endpoint: String = s"$url/api/v1/$artifactType"

  def talkToInterface(command: String, arguments: Option[String] = None): Option[String] = sendAndWaitYaml(request = command, body = arguments)


  def responseToList(result: String, converter: String => T): List[T] = yamlArrayListToList(result).map(single => converter(single))

  def wrapBody(artifact: T): Option[String] = Some(artifactToYaml(artifact))

  def getAllCommandString = s"GET $endpoint"

  def getByNameCommandString(name: String) = s"GET $endpoint/$name"

  def getCreateCommandString = s"POST $endpoint"

  def getUpdateCommandString(name: String) = s"PUT $endpoint/$name"

  def getDeleteCommandString(name: String) = s"DELETE $endpoint/$name"

}



