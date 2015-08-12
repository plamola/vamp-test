package traits

import scala.io._
import scala.language.postfixOps

trait FileAccess {
  def readFile(path: String): String = Source.fromURL(getClass.getResource(path)) mkString
}
