name := "ScalaSatIP"

lazy val settings = Seq(
  version := "0.0.0",

  scalaVersion := "2.13.3",

  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % "1.7.30",
    "org.slf4j" % "jul-to-slf4j" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "com.github.mpilquist" %% "simulacrum" % "0.19.0",
    "org.typelevel" %% "cats-core" % "2.1.1",
    "com.chuusai" %% "shapeless" % "2.3.3",
    "co.fs2" %% "fs2-core" % "2.4.4",
    "co.fs2" %% "fs2-io" % "2.4.4",
    "org.http4s" %% "http4s-core" % "0.21.7",
    "io.monix" %% "monix" % "3.2.2",
    "com.typesafe.akka" %% "akka-actor" % "2.6.9",
    "com.typesafe.akka" %% "akka-remote" % "2.6.9",
    "com.typesafe.akka" %% "akka-stream" % "2.6.9",
    "com.typesafe.akka" %% "akka-http" % "10.2.0",
    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.13.0",
    "com.lihaoyi" %% "fastparse" % "2.3.0",
    "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
    "org.scodec" %% "scodec-bits" % "1.1.20",
    "org.jupnp" % "org.jupnp" % "2.5.2",
    "com.comcast" %% "ip4s-core" % "1.4.0",
    "com.comcast" %% "ip4s-cats" % "1.3.0"
  ),

  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")

  //mainClass in Compile := Some("")
)

lazy val root = project.in(file("."))
  .enablePlugins(
    JavaAppPackaging,
    UniversalPlugin)
  .settings(settings: _*)
