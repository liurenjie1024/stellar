package io.stellar.catalog

import com.softwaremill.macwire.{wire, wireWith}
import io.stellar.IcebergCatalogConfig
import io.stellar.catalog.CatalogModule.backendCatalog
import org.apache.hadoop.conf.Configuration
import org.apache.iceberg.{CatalogProperties, CatalogUtil}
import org.apache.iceberg.catalog.Catalog

import scala.jdk.CollectionConverters.MapHasAsJava

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

