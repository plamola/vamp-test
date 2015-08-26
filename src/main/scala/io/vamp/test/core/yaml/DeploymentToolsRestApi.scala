package io.vamp.test.core.yaml

import io.vamp.core.model.artifact.{Blueprint, Deployment}
import io.vamp.core.model.reader.DeploymentReader
import io.vamp.test.common.DeploymentTools

import scala.language.postfixOps

trait DeploymentToolsRestApi extends DeploymentTools with CoreYamlInterface {


  val endpoint = s"$url/api/v1/deployments"

  override def getDeploymentbyName(name: String): Option[Deployment] = sendAndWaitYaml(request = s"GET $endpoint/$name") match {
    case Some(deployment) => Some(DeploymentReader.read(deployment))
    case None => None
  }

  override def deploy(blueprint: Blueprint): Option[Deployment] = sendAndWaitYaml(request = s"POST $endpoint", Some(artifactToYaml(blueprint))) match {
    case Some(x) => Some(DeploymentReader.read(x))
    case None => None
  }

  override def deploymentUpdate(blueprint: Blueprint, deploymentName: String): Option[Deployment] = sendAndWaitYaml(request = s"PUT $endpoint/$deploymentName", Some(artifactToYaml(blueprint))) match {
    case Some(x) => Some(DeploymentReader.read(x))
    case None => None
  }

  override def undeploy(blueprint: Blueprint, deploymentName: String): Option[Deployment] = sendAndWaitYaml(request = s"DELETE $endpoint/$deploymentName", Some(artifactToYaml(blueprint))) match {
    case Some(updatedDeployment) => Some(DeploymentReader.read(updatedDeployment))
    case None => None
  }

  override def getAllDeployments: List[Deployment] = sendAndWaitYaml(request = s"GET $endpoint") match {
    case Some(deploymentsString) => yamlArrayListToList(deploymentsString).map(deployment => DeploymentReader.read(deployment))
    case None => List.empty
  }

}





