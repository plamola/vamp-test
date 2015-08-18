package io.vamp.test.cli

import org.scalatest._
import io.vamp.test.pulse.CleanableTest
import traits.{ConfigProvider, FileAccess}

import scala.language.postfixOps


class CliGenericSuite extends FlatSpec with CliTools with ConfigProvider with Matchers with FileAccess with Retries {

  val cliVersion: String = config.getString("versions.cli")

  // version
  it should "return the correct version number" taggedAs CleanableTest in {
    val res = execCommand("version")
    res shouldEqual s"CLI version: $cliVersion\n"
  }

  // info
  it should "be able to return the info structure" taggedAs CleanableTest in {
    val res = execCommand("info")
    res.contains(s"Hi, I'm Vamp! How are you?") should be(true)
  }

  // help
  it should "be able to return a help message" taggedAs CleanableTest in {
    val res = execCommand("help")
    res.startsWith("Usage: vamp COMMAND [args..]") should be (true)
  }

}