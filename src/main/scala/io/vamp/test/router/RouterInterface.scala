package io.vamp.test.router

import io.vamp.test.common.{ ConfigProvider, RestSupport, VampInterface }

trait RouterInterface extends VampInterface with ConfigProvider with RestSupport {

  def url: String = config.getString("endpoints.router.url")

  def version: String = config.getString("versions.router")

  def interfaceDescription: String = "Router"

  def readVersionFromInterface: String = sendAndWaitJson(request = s"GET $url/v1/info", None) match {
    case Some(json) ⇒ extractTagContent(json, "\"Version\":\"", "\"")
    case None       ⇒ "No response from info request"
  }

}

