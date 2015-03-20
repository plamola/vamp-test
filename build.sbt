version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.mavenLocal

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-http"    % sprayV,
    "io.spray"            %%  "spray-httpx"    % sprayV,
    "io.spray"            %%  "spray-util"    % sprayV,
    "io.spray"            %%  "spray-client"    % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.1",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "com.typesafe" % "config" % "1.2.1",
    "io.vamp" % "common" % "0.7.0-RC1",
    "io.vamp" %% "pulse" % "0.7.0-RC1"
  )
}

