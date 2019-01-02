package person

import akka.actor._
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor

class PersonRepositoryOnAkkaHttp() extends PersonRestRoutes {}

trait PersonRestRoutes extends Route {
  import akka.http.scaladsl.model.StatusCodes._
  import akka.http.scaladsl.server.Directives._
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
  import dynamodb.{AccountRepositoryOnDynamoDB, PersonRepositoryOnDynamoDB}

  def routes = personRoute
  def personRoute = handleErrors {
    cors() {
      handleErrors {
        pathPrefix("person") {
          pathEndOrSingleSlash {
            get {
              import person.PersonViewJsonProtocol._
              val personRepository = new PersonRepository with PersonRepositoryOnDynamoDB
//              complete(OK, "hoge")
              complete(OK, personRepository.findAllBy("person_1", 1, 1) match {
                case Right(v) => v.toPageJson.compactPrint
                case Left(l) => "error"
              })
            }
          }
        }
      }
    }
  }
}

