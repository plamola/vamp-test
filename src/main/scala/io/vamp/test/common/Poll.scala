package io.vamp.test.common

class Poll[A](body: => A) {
  def until(cond: A => Boolean): A = {
    val result = body
    if (cond(result)) result else until(cond)
  }
}

//TODO Replace this with Scalatest's Eventually

/**
 * Polling a resource without DDOS-ing it
 * Warning: Uses a blocking Thread.sleep call
 */
object Poll {
  def apply[A](body: => A) = new Poll({
    Thread.sleep(200L)
    body
  })


}