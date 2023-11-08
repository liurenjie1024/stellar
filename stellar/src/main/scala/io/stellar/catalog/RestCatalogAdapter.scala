package io.stellar.catalog

import scala.concurrent.Future

import io.stellar.catalog.Endpoints.IcebergErrorResponse
import org.apache.iceberg.catalog.Catalog
import org.apache.iceberg.rest.responses.ConfigResponse

class RestCatalogAdapter(private val catalog: Catalog) {
  def getConfig: Future[Either[IcebergErrorResponse, ConfigResponse]] = {
    Future.successful(Right(ConfigResponse.builder().build()))
  }
}
