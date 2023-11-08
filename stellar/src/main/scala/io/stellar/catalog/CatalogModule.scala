package io.stellar.catalog

import scala.jdk.CollectionConverters.MapHasAsJava

import com.softwaremill.macwire.wire
import com.softwaremill.macwire.wireWith
import io.stellar.IcebergCatalogConfig
import io.stellar.catalog.CatalogModule.backendCatalog
import org.apache.hadoop.conf.Configuration
import org.apache.iceberg.CatalogProperties
import org.apache.iceberg.CatalogUtil
import org.apache.iceberg.catalog.Catalog

class CatalogModule(val config: IcebergCatalogConfig) {
  lazy val catalog: Catalog = wireWith(backendCatalog _)
  lazy val restCatalogAdapter: RestCatalogAdapter = wire[RestCatalogAdapter]
}

object CatalogModule {
  private def backendCatalog(config: IcebergCatalogConfig): Catalog = {
    val props = config.properties.asJava
    props.put(CatalogProperties.CATALOG_IMPL, config.className)
    CatalogUtil.buildIcebergCatalog("stellar_rest_backend", props, new Configuration())
  }
}
