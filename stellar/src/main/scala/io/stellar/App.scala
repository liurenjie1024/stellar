package io.stellar

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.io.StdIn

import com.typesafe.scalalogging.Logger
import io.stellar.catalog.CatalogModule
import io.stellar.catalog.Endpoints
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import sttp.tapir.server.netty.NettyFutureServer
import sttp.tapir.server.netty.NettyFutureServerOptions
import sttp.tapir.server.netty.NettyOptions

object App {
  private val logger = Logger[App]
  def main(args: Array[String]): Unit = {
    logger.info("Starting Stellar REST backend...")

    val config = ConfigSource.default.loadOrThrow[AppConfig]

    val nettyOptions = NettyOptions.default.port(config.server.port)
    val serverLogger = NettyFutureServerOptions
      .defaultServerLog
      .copy(logWhenReceived = true)
      .logWhenHandled(true)
      .logAllDecodeFailures(true)
      .logAllDecodeFailures(true)

    val nettySeverOptions = NettyFutureServerOptions
      .customiseInterceptors
      .serverLog(serverLogger)
      .options
      .nettyOptions(nettyOptions)

    val catalogModule = new CatalogModule(config.iceberg.catalog)

    val program = {
      for {
        binding <- NettyFutureServer(nettySeverOptions)
          .addEndpoints(Endpoints.serverEndpoints(catalogModule.restCatalogAdapter))
          .start()
        _ <- Future {
          logger.info("Rest backend started. Press enter to stop.")
          StdIn.readLine()
        }
        stop <- binding.stop()
      } yield stop
    }

    Await.result(program, Duration.Inf)
  }
}
