package io.stellar

import io.stellar.catalog.Endpoints
import sttp.tapir.server.netty.{NettyFutureServer, NettyFutureServerOptions}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.StdIn
import ExecutionContext.Implicits.global

object App {
  def main(args: Array[String]) = {
    println("Hello, world!")
    val program = {
      for {
        binding <- NettyFutureServer().port(8090).addEndpoints(Endpoints.serverEndpoints).start()
        _ <- Future {
          println(s"Go to http://localhost:${binding.port}/docs to open SwaggerUI. Press ENTER key to exit.")
          StdIn.readLine()
        }
        stop <- binding.stop()
      }
        yield stop
    }

    Await.result(program, Duration.Inf)
  }
}
