import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import tv.kazu.chatwork4s.ChatWorkApiClient
import tv.kazu.chatwork4s.ChatWorkApiResponse
import tv.kazu.chatwork4s.models._
import scala.concurrent.duration.Duration
import scala.util.{ Try, Success, Failure }

object Main extends App {
  def fuga = {
    val token: String = "2170d6533685781b7e66b1673c2e2e09"
    val client = new ChatWorkApiClient(token)
    val f: Future[ChatWorkApiResponse] = client.post("/rooms/88213490/messages", Seq(("body", "hoge")))
    f.onSuccess { case room: Room => println(room) }
    f.onFailure { case t: Throwable => println(t.getMessage()) }
    Await.ready(f, Duration.Inf)
  }

  fuga
}

