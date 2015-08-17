package io.vamp.test.cli

import java.io.{File, PrintWriter}

import traits.ConfigProvider

import scala.language.postfixOps
import scala.sys.process._

trait CliTools extends ConfigProvider {

  val vampHost = config.getString("endpoints.core.url")

  private def stripAnsi(s: String): String = s.replaceAll("\\e\\[[\\d;]*[^\\d;]", "")

  def execCommand(command: String, arguments: Option[String] = None): String = stripAnsi(arguments match {
    case None => s"vamp $command --host $vampHost" !!
    case Some(args) => s"vamp $command --host $vampHost $args" !!
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

