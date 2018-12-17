name := "ScalaSatIP"

lazy val settings = Seq(
  version := "0.0.0",

  scalaVersion := "2.12.8",

  resolvers ++= Seq(
    "lolhens-maven" at "http://artifactory.lolhens.de/artifactory/maven-public/",
    Resolver.url("lolhens-ivy", url("http://artifactory.lolhens.de/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
  ),

  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "org.slf4j" % "jul-to-slf4j" % "1.7.25",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "com.github.mpilquist" %% "simulacrum" % "0.14.0",
    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.3",
    "co.fs2" %% "fs2-core" % "1.0.2",
    "co.fs2" %% "fs2-io" % "1.0.2",
    "io.monix" %% "monix" % "3.0.0-RC2",
    "com.typesafe.akka" %% "akka-actor" % "2.5.19",
    "com.typesafe.akka" %% "akka-remote" % "2.5.19",
    "com.typesafe.akka" %% "akka-stream" % "2.5.19",
    "com.typesafe.akka" %% "akka-http" % "10.1.5",
    "io.circe" %% "circe-core" % "0.10.1",
    "io.circe" %% "circe-generic" % "0.10.1",
    "io.circe" %% "circe-parser" % "0.10.1",
    "com.lihaoyi" %% "fastparse" % "1.0.0",
    "org.scala-lang.modules" %% "scala-xml" % "1.1.1",
    "org.scodec" %% "scodec-bits" % "1.1.7",
    "de.lolhens" %% "scalastringutils" % "0.2.0",
    "org.jupnp" % "org.jupnp" % "2.5.1",
    "com.comcast" %% "ip4s-core" % "1.1.1",
    "com.comcast" %% "ip4s-cats" % "1.1.1"
  ),

  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.9"),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4")

  //mainClass in Compile := Some("")
)

lazy val root = project.in(file("."))
  .enablePlugins(
    JavaAppPackaging,
    UniversalPlugin)
  .settings(settings: _*)
