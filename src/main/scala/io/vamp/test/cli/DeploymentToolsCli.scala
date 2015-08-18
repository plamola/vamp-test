package io.vamp.test.cli

import io.vamp.core.model.artifact.{Blueprint, Deployment}
import io.vamp.core.model.reader.DeploymentReader
import io.vamp.test.common.DeploymentTools
import traits.YamlUtils

import scala.language.postfixOps


trait DeploymentToolsCli extends DeploymentTools with CliTools with YamlUtils {

  override def interfaceDescription: String = "CLI"

  override def getDeploymentbyName(name: String): Option[Deployment] = {
    try {
      Some(DeploymentReader.read(
        execCommand(
          command = "inspect deployment",
          arguments = Some(name)
        )
      ))
    } catch {
      case e: RuntimeException => None
    }
  }

  override def deploy(blueprint: Blueprint): Option[Deployment] =
    try {
      withTemporaryFile(artifactToYaml(blueprint)) { tmpFile =>
        Some(DeploymentReader.read(execCommand(
          command = "deploy",
          arguments = Some(s" --file ${tmpFile.getAbsoluteFile}"))
        ))
      }
    } catch {
      case e: RuntimeException => None
    }

  override def deploymentUpdate(blueprint: Blueprint, deploymentName: String): Option[Deployment] =
    try {
      withTemporaryFile(artifactToYaml(blueprint)) { tmpFile =>
        Some(DeploymentReader.read(execCommand(
          command = "deploy",
          arguments = Some(s"--deployment $deploymentName --file ${tmpFile.getAbsoluteFile}"))
        ))
      }
    } catch {
      case e: RuntimeException => None
    }

  override def undeploy(deployment: Deployment): Option[Deployment] =
    try {
      Some(DeploymentReader.read(execCommand(
        command = "undeploy",
        arguments = Some(deployment.name))
      ))
    } catch {
      case e: RuntimeException => None
    }
}





