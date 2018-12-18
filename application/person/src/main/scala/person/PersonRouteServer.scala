package person

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try
import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.server
import akka.http.scaladsl.server.{HttpApp, Route}


object PersonRouteServer {
  def apply(routes: server.Route): PersonRouteServer = new PersonRouteServer(routes)
}

class PersonRouteServer(r: server.Route) extends HttpApp {
  override protected val routes: server.Route = r
  override protected def postServerShutdown(attempt: Try[Done], system: ActorSystem): Unit = {
    super.postServerShutdown(attempt, system)
    system.terminate()
    Await.result(system.whenTerminated, 30.seconds)
    ()
  }
}
