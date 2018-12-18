package apigateway

import scala.beans.BeanProperty

case class Response(@BeanProperty body: String, @BeanProperty headers: Map[String, String], @BeanProperty statusCode: Int)
object Response {
  val contentJson = "Content-Type" -> "application/json"
  def apply(body: String, headers: Map[String, String] = Map(contentJson), statusCode: Int = 200) = {
    new Response(body, headers, statusCode)
  }
}
