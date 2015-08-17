package io.vamp.test.core.api

import io.vamp.core.model.artifact.{Blueprint, Deployment}
import io.vamp.core.model.reader.DeploymentReader
import io.vamp.test.common.DeploymentTools
import traits.YamlUtils

import scala.language.postfixOps

trait DeploymentToolsRestApi extends DeploymentTools with CoreTools with YamlUtils {

  override def interfaceDescription : String = "Rest API"

  override def getDeploymentbyName(name: String): Option[Deployment] = sendAndWaitYaml(request = s"GET $url/api/v1/deployments/$name") match {
    case Some(deployment) => Some(DeploymentReader.read(deployment))
    case None => None
  }

  override def deploy(blueprint: Blueprint): Option[Deployment] = sendAndWaitYaml(request = s"POST $url/api/v1/deployments", Some(artifactToYaml(blueprint))) match {
    case Some(x) => Some(DeploymentReader.read(x))
    case None => None
  }

  override def deploymentUpdate(blueprint: Blueprint, deploymentName: String): Option[Deployment] = sendAndWaitYaml(request = s"PUT $url/api/v1/deployments/$deploymentName", Some(artifactToYaml(blueprint))) match {
    case Some(x) => Some(DeploymentReader.read(x))
    case None => None
  }

  override def undeploy(deployment: Deployment): Option[Deployment] = sendAndWaitYaml(request = s"DELETE $url/api/v1/deployments/${deployment.name}", Some(artifactToYaml(deployment))) match {
    case Some(updatedDeployment) => Some(DeploymentReader.read(updatedDeployment))
    case None => None
  }

}





