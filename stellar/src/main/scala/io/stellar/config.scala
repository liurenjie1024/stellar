
package io.stellar

case class IcebergCatalogConfig(className: String, properties: Map[String, String])
case class IcebergConfig(catalog: IcebergCatalogConfig)
case class ServerConfig(port: Int)
case class AppConfig(server: ServerConfig, iceberg: IcebergConfig)
