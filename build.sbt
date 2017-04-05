name := "ScalaSatIP"

lazy val settings = Seq(
  version := "0.0.0",

  scalaVersion := "2.12.1",

  resolvers := Seq("Artifactory" at "http://lolhens.no-ip.org/artifactory/libs-release/"),

  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % "2.12.1",
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "org.slf4j" % "jul-to-slf4j" % "1.7.25",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "com.github.mpilquist" %% "simulacrum" % "0.10.0",
    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "io.monix" %% "monix" % "2.2.4",
    "io.monix" %% "monix-cats" % "2.2.4",
    "com.typesafe.akka" %% "akka-actor" % "2.4.17",
    "com.typesafe.akka" %% "akka-remote" % "2.4.17",
    "com.typesafe.akka" %% "akka-stream" % "2.4.17",
    "com.typesafe.akka" %% "akka-http" % "10.0.5",
    "io.spray" %% "spray-json" % "1.3.3",
    "com.github.fommil" %% "spray-json-shapeless" % "1.3.0",
    "com.lihaoyi" %% "fastparse" % "0.4.2",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    "org.scodec" %% "scodec-bits" % "1.1.4"//,
    //"org.fourthline.cling" % "cling-core" % "2.1.2-SNAPSHOT"
  ),

  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),

  mainClass in Compile := Some(""),

  scalacOptions ++= Seq("-Xmax-classfile-name", "254")
)

lazy val root = project.in(file("."))
  .enablePlugins(
    JavaAppPackaging,
    UniversalPlugin)
  .settings(settings: _*)
