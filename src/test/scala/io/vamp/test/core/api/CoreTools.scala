package io.vamp.test.core.api

import traits.ConfigProvider


trait CoreTools extends ConfigProvider {

  implicit val url: String = config.getString("endpoints.core.url")


}

