package io.vamp.test.json

import org.json4s.Formats

trait JsonFormatsProvider {
  implicit val formats: Formats
}

