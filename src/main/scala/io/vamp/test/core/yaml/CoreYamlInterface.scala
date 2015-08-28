package io.vamp.test.core.yaml

import io.vamp.test.common.{ YamlUtils, ConfigProvider, RestSupport, VampInterface }

trait CoreYamlInterface extends VampInterface with ConfigProvider with RestSupport with YamlUtils {

  def url: String = config.getString("endpoints.core.url")

  def version: String = config.getString("versions.core")

  def interfaceDescription: String = "Rest API [yaml]"

  def readVersionFromInterface: String = sendAndWaitYaml(request = s"GET $url/api/v1/info", None) match {
    case Some(yaml) ⇒ extractTagContent(yaml, "\nversion: ", "\n")
    case None       ⇒ "No version detected"
  }

}

