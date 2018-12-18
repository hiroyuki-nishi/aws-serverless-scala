package person

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success}

object Server extends App with RequestTimeout {
  private def executeServer(): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val ec = system.dispatcher
    implicit val materialize = ActorMaterializer()
    PersonRouteServer(new PersonRepositoryOnAkkaHttp().routes).startServer(
      ConfigFactory.load().getString("http.host"),
      ConfigFactory.load().getInt("http.port"),
      system
    )
  }
  executeServer()
}

trait RequestTimeout {
  import scala.concurrent.duration.Duration
  private def requestTimeout(config: Config): Timeout = {
    FiniteDuration(
      Duration(config.getString("akka.request-timeout")).length,
      Duration(config.getString("akka.request-timeout")).unit
    )
  }
}

