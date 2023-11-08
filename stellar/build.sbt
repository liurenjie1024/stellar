val tapirVersion = "1.5.1"
val icebergVersion = "1.4.1"
val scalaTestVersion = "3.2.17"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "stellar",
    version := "0.1.0-SNAPSHOT",
    organization := "io.stellar",
    scalaVersion := "2.13.12",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Werror"),
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "org.xerial" % "sqlite-jdbc" % "3.43.0.0",
      "org.postgresql" % "postgresql" % "42.5.4",
      "com.softwaremill.macwire" %% "macros" % "2.5.9" % "provided",
      "org.apache.iceberg" % "iceberg-api" % icebergVersion,
      "org.apache.iceberg" % "iceberg-core" % icebergVersion,
      "org.apache.iceberg" % "iceberg-aws-bundle" % icebergVersion,
      "org.apache.iceberg" % "iceberg-aws" % icebergVersion,
      "org.apache.hadoop" % "hadoop-common" % "3.3.6",
      "ch.qos.logback" % "logback-classic" % "1.2.10",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "com.github.pureconfig" %% "pureconfig" % "0.17.4",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
      "org.scalatest" %% "scalatest-funspec" % scalaTestVersion % Test,
      "com.softwaremill.sttp.client3" %% "circe" % "3.9.0",
    )
  )
)
