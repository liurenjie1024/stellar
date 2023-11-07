package io.stellar

import com.typesafe.scalalogging.Logger
import io.stellar.catalog.Endpoints
import sttp.tapir.server.netty.{NettyFutureServer, NettyFutureServerOptions, NettyOptions}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.StdIn
import ExecutionContext.Implicits.global


object App {
  private val logger = Logger[App]
  def main(args: Array[String]) = {
    println("Hello, world!")
    logger.debug(s"First debug info!")

    val nettyOptions = NettyOptions.default.port(8090)
    val serverLogger = NettyFutureServerOptions.defaultServerLog
      .copy(logWhenReceived = true)
      .logWhenHandled(true)
      .logAllDecodeFailures(true)
      .logAllDecodeFailures(true)

    val nettySeverOptions = NettyFutureServerOptions.customiseInterceptors
      .serverLog(serverLogger)
      .options
      .nettyOptions(nettyOptions)

    val program = {
      for {
        binding <- NettyFutureServer(nettySeverOptions)
          .addEndpoints(Endpoints.serverEndpoints)
          .start()
        _ <- Future {
          println(s"Go to http://localhost:${binding.port}/docs to open SwaggerUI. Press ENTER key to exit.")
          StdIn.readLine()
        }
        stop <- binding.stop()
      } yield stop
    }

    Await.result(program, Duration.Inf)
  }
}
