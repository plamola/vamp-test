package io.vamp.test.core

import io.vamp.test.common.VampInterface
import traits.{ConfigProvider, RestSupport}


trait CoreInterface extends VampInterface with ConfigProvider with RestSupport {

  def url: String = config.getString("endpoints.core.url")

  def version: String = config.getString("versions.core")

  def interfaceDescription: String = "Rest API"

  def readVersionFromInterface: String = sendAndWaitYaml(request = s"GET $url/api/v1/info", None) match {
    case Some(yaml) => extractTagContent(yaml, "\nversion: ", "\n")
    case None => "No version detected"
  }


}

