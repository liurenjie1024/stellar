package io.stellar.catalog

import io.stellar.catalog.Codecs._
import org.apache.iceberg.rest.requests.UpdateTableRequest
import org.apache.iceberg.rest.responses.LoadTableResponse
import sttp.model.StatusCode
import sttp.tapir.EndpointIO.annotations._
import sttp.tapir._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import io.circe.generic.auto._

import scala.concurrent.Future

object Endpoints {
  private val baseEndpoint = endpoint.in("api" / "catalog")

  @endpointInput("v1/namespaces/{namespace}/tables/{table}")
  case class UpdateTableInput(@path namespace: String, @path table: String, @jsonbody body: UpdateTableRequest)
  case class ErrorResponse(message: String, `type`: String, code: Int, stack: Array[String])
//  case class ErrorResponse(message: String, `type`: String, code: Int)
  case class IcebergErrorResponse(@statusCode code: StatusCode, @jsonbody error: ErrorResponse)


  val updateTableEndpoint: PublicEndpoint[UpdateTableInput, IcebergErrorResponse, LoadTableResponse, Any] = baseEndpoint.post
    .name("updateTable")
    .summary("Commit updates to a table")
    .tag("Catalog API")
    .in(EndpointInput.derived[UpdateTableInput])
    .out(statusCode(StatusCode.Ok).and(customCodecJsonBody[LoadTableResponse]))
    .errorOut(EndpointOutput.derived[IcebergErrorResponse])

  val updateTableServerEndpoint: ServerEndpoint[Any, Future] = updateTableEndpoint.serverLogic { _ =>
    Future.successful[Either[IcebergErrorResponse, LoadTableResponse]](
      Left(IcebergErrorResponse(StatusCode.BadRequest, ErrorResponse("test", "test", 400, Array()))))

  }

  @endpointInput("v1/namespaces/{namespace}/tables/{table}")
  case class GetTableInput(@path namespace: String, @path table: String)
  case class GetTableOutput(name: String)

  val getTableEndpoint: PublicEndpoint[GetTableInput, IcebergErrorResponse, GetTableOutput, Any] = baseEndpoint.get
    .name("getTable")
    .in(EndpointInput.derived[GetTableInput])
    .out(statusCode(StatusCode.Ok).and(jsonBody[GetTableOutput]))
    .errorOut(EndpointOutput.derived[IcebergErrorResponse])

  val getTableServerEndpoint: ServerEndpoint[Any, Future] = getTableEndpoint.serverLogic { _ =>
    Future.successful[Either[IcebergErrorResponse, GetTableOutput]](
      Right(GetTableOutput("xx")))
  }

  val serverEndpoints = List(updateTableServerEndpoint, getTableServerEndpoint)
}
