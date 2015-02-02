name := "play-garoon"

version := "1.0-SNAPSHOT"

resolvers += "github mtgto/garoon" at "http://mtgto.github.io/garoon/maven/"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "net.mtgto" %% "garoon" % "0.2.0-SNAPSHOT"
)

dependencyOverrides += "org.apache.ws.commons.axiom" % "axiom-impl" % "1.2.14"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

