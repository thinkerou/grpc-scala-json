# grpc-scala-json

Simple example of [grpc-java](https://github.com/grpc/grpc-java) with [ScalaPB](https://github.com/scalapb/ScalaPB) and [scalapb-json4s](https://github.com/scalapb/scalapb-json4s).

### Server

Run the following command:

```bash
➜  cd grpc-scala-json
➜  sbt
sbt:grpc-scala-json> compile
sbt:grpc-scala-json> run
[warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list

Multiple main classes detected, select one to run:

 [1] helloworld.HelloWorldClient
 [2] helloworld.HelloWorldServer
[info] Packaging /Users/thinkerou/grpc-scala-json/target/scala-2.12/grpc-scala-json_2.12-0.1.jar ...
[info] Done packaging.

Enter number: 2

[info] Running helloworld.HelloWorldServer
Apr 28, 2018 4:16:29 PM helloworld.HelloWorldServer helloworld$HelloWorldServer$$start
信息: Server started and listening on 50052

```

### Client

Run the following command and input the path of JSON file:

```bash
➜  cd grpc-scala-json
➜  sbt
sbt:grpc-scala-json> runMain helloworld.HelloWorldClient ./data/helloworld.json
```

Default path of `helloworld.json` is `./data/helloworld.json`.
