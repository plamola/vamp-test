package traits

import scala.io._
/**
 * Created by lazycoder on 17/03/15.
 */
trait FileAccess {
  def readFile(path: String): String = Source.fromURL(getClass.getResource(path)) mkString
}
