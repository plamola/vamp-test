package io.vamp.test.cli

import org.scalatest._
import io.vamp.test.pulse.CleanableTest
import traits.FileAccess

import scala.language.postfixOps


class CliGenericSuite extends FlatSpec with CliInterface with Matchers with FileAccess with Retries {


  // version
  it should "return the correct version number" taggedAs CleanableTest in {
    val res = execCommand("version")
    res shouldEqual s"CLI version: $version\n"
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