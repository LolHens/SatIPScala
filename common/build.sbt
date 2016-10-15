libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  //"org.scala-lang" % "scala-compiler" % "2.11.8",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.4",
  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-remote" % "2.4.7",
  "com.typesafe.akka" %% "akka-stream" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.7",
  "de.svenkubiak" % "jBCrypt" % "0.4.1",
  "org.typelevel" %% "cats" % "0.7.2",
  "com.github.mpilquist" %% "simulacrum" % "0.8.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "io.monix" %% "monix" % "2.0.4",
  "io.monix" %% "monix-cats" % "2.0.4",
  "io.spray" %% "spray-json" % "1.3.2",
  "com.github.fommil" %% "spray-json-shapeless" % "1.2.0"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.0")