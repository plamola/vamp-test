package io.vamp.test.common

import io.vamp.common.http.RestClient
import io.vamp.common.http.RestClient.Method

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import scala.language.{ implicitConversions, postfixOps }
import scala.util.{ Failure, Success }

trait RestSupport {

  def timeout: Duration = 30 seconds

  def sendAndWaitYaml(request: String, body: Option[String] = None)(implicit m: Manifest[String]): Option[String] =
    sendAndWait(request, body, List("Accept" -> "application/x-yaml", "Content-Type" -> "application/x-yaml", RestClient.acceptEncodingIdentity))

  def sendAndWaitJson(request: String, body: Option[String] = None)(implicit m: Manifest[String]): Option[String] =
    sendAndWait(request, body, List("Accept" -> "application/json", "Content-Type" -> "application/json", RestClient.acceptEncodingIdentity))

  private def sendAndWait(request: String, body: AnyRef, headers: List[(String, String)])(implicit m: Manifest[String]): Option[String] = {
    try {
      val upper = request.toUpperCase
      val method = Method.values.find(method ⇒ upper.startsWith(s"${method.toString} ")).getOrElse(Method.GET)
      val url = if (upper.startsWith(s"${method.toString} ")) request.substring(s"${method.toString} ".length) else request

      val futureResult: Future[String] = RestClient.http[String](method, url, body, headers)

      // Block until response ready (nothing else to do anyway)
      Await.result(futureResult, timeout)
      futureResult.value.get match {
        case Success(result) ⇒ Some(result)
        case Failure(error)  ⇒ None
      }
    } catch {
      case e: Exception ⇒ None
    }
  }

}
