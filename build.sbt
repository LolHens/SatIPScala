name := "ScalaSatIP"

lazy val settings = Seq(
  version := "0.0.0",

  scalaVersion := "2.12.4",

  resolvers ++= Seq(
    "artifactory-maven" at "http://lolhens.no-ip.org/artifactory/maven-public/",
    Resolver.url("artifactory-ivy", url("http://lolhens.no-ip.org/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "org.slf4j" % "jul-to-slf4j" % "1.7.25",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "com.github.mpilquist" %% "simulacrum" % "0.11.0",
    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "io.monix" %% "monix" % "2.3.0",
    "io.monix" %% "monix-cats" % "2.3.0",
    "com.typesafe.akka" %% "akka-actor" % "2.5.6",
    "com.typesafe.akka" %% "akka-remote" % "2.5.6",
    "com.typesafe.akka" %% "akka-stream" % "2.5.6",
    "com.typesafe.akka" %% "akka-http" % "10.0.10",
    "io.circe" %% "circe-core" % "0.8.0",
    "io.circe" %% "circe-generic" % "0.8.0",
    "io.circe" %% "circe-parser" % "0.8.0",
    "com.lihaoyi" %% "fastparse" % "1.0.0",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    "org.scodec" %% "scodec-bits" % "1.1.5",
    "de.lolhens" %% "scalastringutils" % "0.2.0",
    "org.fourthline.cling" % "cling-core" % "2.1.1"
  ),

  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"),

  mainClass in Compile := Some(""),

  scalacOptions ++= Seq("-Xmax-classfile-name", "254")
)

lazy val root = project.in(file("."))
  .enablePlugins(
    JavaAppPackaging,
    UniversalPlugin)
  .settings(settings: _*)
