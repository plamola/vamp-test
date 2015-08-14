package io.vamp.test.core.api

import traits.{RestSupport, ConfigProvider}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits._

trait CoreTools extends RestSupport with ConfigProvider {

  implicit def executionContext: ExecutionContext = {
    global
  }

  implicit val url: String = config.getString("endpoints.core.url")


}

