logLevel := Level.Warn

resolvers ++= Seq(
  "lolhens-maven" at "http://artifactory.lolhens.de/artifactory/maven-public/",
  Resolver.url("lolhens-ivy", url("http://artifactory.lolhens.de/artifactory/ivy-public/"))(Resolver.ivyStylePatterns)
)

//addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.12")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.8")
