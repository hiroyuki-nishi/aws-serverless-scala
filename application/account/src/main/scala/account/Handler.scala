package account

import apigateway.{ApiGatewayResponse, Request, Response}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import dynamodb.AccountRepositoryOnDynamoDB

import scala.collection.JavaConverters

class Handler extends RequestHandler[Request, Response] {
  def getAccounts(): String = {
    import account.AccountViewJsonProtocol._

    val accountRepository = new AccountRepository with AccountRepositoryOnDynamoDB
    (for {
      t <- accountRepository.findAllBy("person_1", 1, 1)
    } yield t) match {
      case Right(r) => r.toPageJson.compactPrint
      case Left(_) => "Error"
    }
  }

  def handleRequest(input: Request, context: Context): Response = {
    return Response(getAccounts())
  }
}

// TODO
class ApiGatewayHandler extends RequestHandler[Request, ApiGatewayResponse] {
  def handleRequest(input: Request, context: Context): ApiGatewayResponse = {
    val headers = Map("x-custom-response-header" -> "my custom response header value")
    ApiGatewayResponse(200, "Go Serverless v1.0! Your function executed successfully!",
      JavaConverters.mapAsJavaMap[String, Object](headers),
      true)
  }
}
