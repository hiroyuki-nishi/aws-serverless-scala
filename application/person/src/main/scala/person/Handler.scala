package person

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import dynamodb.PersonRepositoryOnDynamoDB
import scala.collection.JavaConverters

import apigateway.{ApiGatewayResponse, Request, Response}

class Handler extends RequestHandler[Request, Response] {
  private def getPersons(): String = {
    import person.PersonViewJsonProtocol._
    val personRepository = new PersonRepository with PersonRepositoryOnDynamoDB
    (for {
      t <- personRepository.findAllBy("person_1", 1, 1)
    } yield t) match {
      case Right(r) => r.toPageJson.compactPrint
      case Left(_) => "Error" //TODO convert ErrorClass
    }
  }

  def handleRequest(input: Request, context: Context): Response = {
    return Response(getPersons())
  }
}

// TODO delete
class ApiGatewayHandler extends RequestHandler[Request, ApiGatewayResponse] {
  def handleRequest(input: Request, context: Context): ApiGatewayResponse = {
    val headers = Map("x-custom-response-header" -> "my custom response header value")
    ApiGatewayResponse(200, "Go Serverless v1.0! Your function executed successfully!",
      JavaConverters.mapAsJavaMap[String, Object](headers), true)
  }
}
