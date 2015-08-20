package io.vamp.test.pulse

import io.vamp.test.common.VampInterface
import traits.{ConfigProvider, RestSupport}


trait PulseInterface extends VampInterface with ConfigProvider with RestSupport {

  def url: String = config.getString("endpoints.pulse.url")

  def version: String = config.getString("versions.pulse")

  def interfaceDescription: String = "Pulse API"

  def readVersionFromInterface: String = sendAndWaitYaml(request = s"GET $url/api/v1/info", None) match {
    case Some(yaml) => extractTagContent(yaml, "\nversion: ", "\n")
    case None => "No version detected"
  }


}

