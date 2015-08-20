package io.vamp.test.core.yaml


import io.vamp.core.model.artifact.Artifact
import io.vamp.core.model.reader.YamlReader
import io.vamp.test.common.ArtifactTools


trait ArtifactOperations[T <: Artifact] extends ArtifactTools[T] with CoreYamlInterface  {

  val endpointName  : String

  private def endpoint : String =s"$url/api/v1/$endpointName"

  def responseConverter : YamlReader[T]

  def getAll : List[T] =sendAndWaitYaml(request = s"GET $endpoint") match {
    case Some(result) => yamlArrayListToList(result).map(single=> responseConverter.read(single))
    case None => List.empty
  }

  def getByName(name: String): Option[T] = sendAndWaitYaml(request = s"GET $endpoint/$name") match {
    case Some(result) => Some(responseConverter.read(result))
    case None => None
  }

  def create(artifact: T) = sendAndWaitYaml(request = s"POST $endpoint") match {
    case Some(result) => Some(responseConverter.read(result))
    case None => None
  }

  def update(name: String,artifact: T) = sendAndWaitYaml(request = s"PUT $endpoint/$name") match {
    case Some(result) => Some(responseConverter.read(result))
    case None => None
  }

  def delete(name: String) = sendAndWaitYaml(request = s"DELETE $endpoint/$name")

 }





