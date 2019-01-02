import sbt.Keys._
import sbt._
import sbtrelease.Version

resolvers += Resolver.sonatypeRepo("public")
scalaVersion := "2.12.2"
releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings")

val commonLibraryDependencies = Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.20.0",
  "ch.megard" %% "akka-http-cors" % "0.3.1"
)

val testDependencies = Seq(
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
)

lazy val root = (project in file(".")).aggregate(person, account)

lazy val domain = (project in file("domain"))
  .settings(
    name := "domain",
    libraryDependencies ++= Seq(
      "io.spray" %%  "spray-json" % "1.3.5"
    )
  )

lazy val person = (project in file("application/person")).
  enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)
  .settings(
    name := "person",
    libraryDependencies ++= commonLibraryDependencies ++ testDependencies,
    assemblyJarName in assembly := "person.jar",
    maintainer in Docker := "k636362",
    packageName in Docker := "dockerised-akka-http",
    dockerBaseImage := "openjdk:8-jre-alpine",
    dockerExposedPorts := Seq(8080),
    mainClass in assembly := Some("person.Server")
  )
  .dependsOn(domain, infrastracture)

lazy val account = (project in file("application/account"))
  .settings(
    name := "account",
    libraryDependencies ++= commonLibraryDependencies
  )
  .settings(assemblyJarName in assembly := "account.jar")
  .dependsOn(domain, infrastracture)

lazy val infrastracture = (project in file("infrastracture"))
  .settings(
    name := "infrastracture",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.29"
    )
  ).dependsOn(domain)

