package io.stellar.catalog

import io.circe.generic.auto._
import io.stellar.catalog.Codecs._
import org.apache.iceberg.rest.requests.UpdateTableRequest
import org.apache.iceberg.rest.responses.{ConfigResponse, LoadTableResponse}
import sttp.model.StatusCode
import sttp.tapir.EndpointIO.annotations._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint

import scala.concurrent.Future

object Endpoints {
  private val baseEndpoint = endpoint.in("api" / "catalog")

  @endpointInput("v1/namespaces/{namespace}/tables/{table}")
  case class UpdateTableInput(@path namespace: String, @path table: String, @jsonbody body: UpdateTableRequest)
  case class ErrorResponse(message: String, `type`: String, code: Int, stack: Array[String])
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

  private val getTableEndpoint: PublicEndpoint[GetTableInput, IcebergErrorResponse, GetTableOutput, Any] = baseEndpoint.get
    .name("getTable")
    .in(EndpointInput.derived[GetTableInput])
    .out(statusCode(StatusCode.Ok).and(jsonBody[GetTableOutput]))
    .errorOut(EndpointOutput.derived[IcebergErrorResponse])

  private def getTableServerEndpoint: ServerEndpoint[Any, Future] = getTableEndpoint.serverLogic { _ =>
    Future.successful[Either[IcebergErrorResponse, GetTableOutput]](
      Right(GetTableOutput("xx")))
  }

  @endpointInput("v1/config")
  private case class GetConfigInput(@query warehouse: Option[String])

  private val getConfigEndpoint: PublicEndpoint[GetConfigInput, IcebergErrorResponse, ConfigResponse, Any] = baseEndpoint.get
    .name("getConfig")
    .in(EndpointInput.derived[GetConfigInput])
    .out(statusCode(StatusCode.Ok).and(stringBodyUtf8AnyFormat(configResponseCodec)))
    .errorOut(EndpointOutput.derived[IcebergErrorResponse])

  private def getConfigServerEndpoint(adapter: RestCatalogAdapter): ServerEndpoint[Any, Future] = getConfigEndpoint.serverLogic { _ =>
    adapter.getConfig
  }


  def serverEndpoints(adapter: RestCatalogAdapter): List[ServerEndpoint[Any, Future]] = {
    List(
      updateTableServerEndpoint,
      getTableServerEndpoint,
      getConfigServerEndpoint(adapter)
    )
  }
}
