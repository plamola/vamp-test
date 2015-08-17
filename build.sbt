version       := "0.1"

resolvers in ThisBuild += Resolver.url("magnetic-io ivy resolver", url("http://dl.bintray.com/magnetic-io/vamp"))(Resolver.ivyStylePatterns)

resolvers in ThisBuild ++= Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.jcenterRepo
)


libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  val slf4jVersion = "1.7.10"
  val logbackVersion = "1.1.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-http"    % sprayV,
    "io.spray"            %%  "spray-httpx"    % sprayV,
    "io.spray"            %%  "spray-util"    % sprayV,
    "io.spray"            %%  "spray-client"    % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.1",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe" % "config" % "1.2.1",
    "io.vamp" %% "common" % "0.7.9",
    "io.vamp" %% "core-model" % "0.7.9",
    "io.vamp" %% "pulse-server" % "0.7.9"
  )
}


parallelExecution in Test := false

scalaVersion  := "2.11.7"

scalacOptions += "-target:jvm-1.8"

javacOptions ++= Seq("-encoding", "UTF-8")

scalacOptions in ThisBuild ++= Seq(Opts.compile.deprecation, Opts.compile.unchecked) ++
  Seq("-Ywarn-unused-import", "-Ywarn-unused", "-Xlint", "-feature")



