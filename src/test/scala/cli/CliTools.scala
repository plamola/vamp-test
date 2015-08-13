package cli


import traits.ConfigProvider

import scala.language.postfixOps
import scala.sys.process._

trait CliTools extends ConfigProvider {

  implicit val vampHost = config.getString("endpoints.core.url")

  private def stripAnsi(s: String): String = s.replaceAll("\\e\\[[\\d;]*[^\\d;]", "")

  def execCommand(command: String, arguments: Option[String] = None)(implicit vampHost: String): String = stripAnsi(arguments match {
    case None => s"vamp $command --host $vampHost" !!
    case Some(args) => s"vamp $command $args --host $vampHost" !!
  })

}

