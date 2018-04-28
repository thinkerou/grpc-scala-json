package helloworld

import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}

import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}

import scalapb.GeneratedMessage

import scalapb.json4s.JsonFormat

import scala.io.Source

import protos.helloworld.GreeterGrpc.GreeterBlockingStub
import protos.helloworld.{GreeterGrpc, HelloRequest, HelloReply}

/**
 * A simple client that requests a greeter from the {@link HelloWorldServer}.
 */
object HelloWorldClient {
  private val logger = Logger.getLogger(getClass.getName)

  def apply(host: String, port: Int): HelloWorldClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
    val blockingStub = GreeterGrpc.blockingStub(channel)
    new HelloWorldClient(channel, blockingStub)
  }

  /**
   * main function for making request
   */
  def main(args: Array[String]): Unit = {
    var path = "./data/helloworld.json"
    if (args.length > 0) path = args(0)
    logger.info(s"Input path: ${path}")

    val json: String = Source.fromFile(path).getLines.mkString
    val proto: HelloRequest = JsonFormat.fromJsonString[HelloRequest](json)

    val client = HelloWorldClient("localhost", 50052)

    try {
      client.sayHello(proto)
    } finally {
      client.shutdown()
    }
  }
}

class HelloWorldClient private(private val channel: ManagedChannel,
                               private val blockingStub: GreeterGrpc.GreeterBlockingStub) {
  def shutdown(): Boolean = channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)

  def sayHello(request: HelloRequest): Unit = {
    try {
      val response: HelloReply = blockingStub.sayHello(request)
      HelloWorldClient.logger.info(s"response: $response")
    } catch {
      case e: StatusRuntimeException => HelloWorldClient.logger.warning(s"GRPC request error: ${e.getMessage}")
    }
  }
}

