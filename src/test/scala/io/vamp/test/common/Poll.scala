package io.vamp.test.common


class Poll[A](body: => A) {
  def until(cond: A => Boolean): A = {
    val result = body
    if (cond(result)) result else until(cond)
  }
}

object Poll {
  def apply[A](body: => A) = new Poll({
    Thread.sleep(200L)
    body
  })


}