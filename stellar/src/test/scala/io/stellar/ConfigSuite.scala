package io.stellar

import org.scalatest.funsuite.AnyFunSuite
import pureconfig.ConfigSource
import pureconfig._
import pureconfig.generic.auto._

class ConfigSuite extends AnyFunSuite {
  test("Load config") {
    val config = ConfigSource.resources("config.json").loadOrThrow[AppConfig]

    val expectedConfig = AppConfig(
      ServerConfig(port = 8090),
      IcebergConfig(IcebergCatalogConfig("Test", Map("a"->"b", "c" -> "d")))
    )

    assert(config == expectedConfig)
  }
}
