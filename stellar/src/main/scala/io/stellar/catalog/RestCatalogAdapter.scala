package io.stellar.catalog

import io.stellar.catalog.Endpoints.IcebergErrorResponse
import org.apache.iceberg.catalog.Catalog
import org.apache.iceberg.rest.responses.ConfigResponse

import scala.concurrent.Future

class RestCatalogAdapter(private val catalog: Catalog) {
  def getConfig: Future[Either[IcebergErrorResponse, ConfigResponse]]  = {
    Future.successful(Right(ConfigResponse.builder().build()))
  }
}
