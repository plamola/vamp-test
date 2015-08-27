package io.vamp.test.cli.yaml

import java.io.{File, PrintWriter}

import io.vamp.test.common.{ConfigProvider, VampInterface, YamlUtils}

import scala.language.postfixOps
import scala.sys.process._

trait CliYamlInterface extends VampInterface with ConfigProvider with YamlUtils {

  def url: String = config.getString("endpoints.core.url")

  // Used as the Vamp host
  def version: String = config.getString("versions.cli")

  def interfaceDescription: String = "CLI"

  def readVersionFromInterface: String = extractTagContent(execCommand("version"), "CLI version: ", "\n")

  private def stripAnsi(s: String): String = s.replaceAll("\\e\\[[\\d;]*[^\\d;]", "")

  def execCommand(command: String, arguments: Option[String] = None): String = stripAnsi(arguments match {
    case None => s"vamp $command --host $url" !!
    case Some(args) => s"vamp $command --host $url $args" !!
  })


  def withTemporaryFile[T](content: String)(action: File => T): T = {
    val file = dumpToFile(content)
    try {
      action(file)
    }
    finally {
      file.delete()
    }
  }

  private def dumpToFile(content: String): File = {
    val file = File.createTempFile("vamp-test", ".test")
    val writer = new PrintWriter(file)
    writer.write(content)
    writer.close()
    file
  }

}

