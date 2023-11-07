val tapirVersion = "1.5.1"
val icebergVersion = "1.4.1"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "stellar",
    version := "0.1.0-SNAPSHOT",
    organization := "io.stellar",
    scalaVersion := "2.13.12",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "org.apache.iceberg" % "iceberg-core" % icebergVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.10",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "com.github.pureconfig" %% "pureconfig" % "0.17.4",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "com.softwaremill.sttp.client3" %% "upickle" % "3.8.15" % Test
    )
  )
)
