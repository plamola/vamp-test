package io.vamp.test.common

trait VampInterface {

  def interfaceDescription: String

  def url: String

  def version: String

  def readVersionFromInterface: String

  protected def extractTagContent(content: String, startTag: String, endTag: String): String = {
    val endOfStartTag = content.indexOf(startTag) + startTag.length
    content.substring(endOfStartTag, content.indexOf(endTag, endOfStartTag + 1))
  }

}
