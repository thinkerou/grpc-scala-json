package helloworld

import java.io.IOException
import java.util.logging.Logger

import io.grpc.{Server, ServerBuilder}
import io.grpc.stub.StreamObserver

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

import protos.helloworld.{GreeterGrpc, HelloRequest, HelloReply}

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
object HelloWorldServer {
  private val logger = Logger.getLogger(getClass.getName)
  private val port = 50052

  def main(agrs: Array[String]): Unit = {
    val server = new HelloWorldServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }
}

class HelloWorldServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Option[Server] = None

  private def start(): Unit = {
    server = Some(
      ServerBuilder
        .forPort(HelloWorldServer.port)
        .addService(GreeterGrpc.bindService(new GreeterImpl, executionContext))
        .build
        .start
    )

    HelloWorldServer.logger.info(s"Server started and listening on ${HelloWorldServer.port}")

    sys.addShutdownHook {
      System.err.println("Shutting down GRPC server")
      self.stop()
      System.err.println("Server shut down")
    }
  }

  private def stop(): Unit = {
    for (s <- server) {
      s.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    for (s <- server) {
      s.awaitTermination()
    }
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(request: HelloRequest) = {
      HelloWorldServer.logger.info(s"Requested with $request")

      var logid = Random.nextInt
      if (logid < 0) logid *= -1

      val response = HelloReply(
        logid = logid,
        message = s"Hello ${request.name}, your info includes phone:${request.phone} and location:${request.location}"
      )
      Future.successful(response)
    }
  }
}
