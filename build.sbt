name := "play-garoon"

version := "1.0-SNAPSHOT"

resolvers += "github mtgto/garoon" at "http://mtgto.github.io/garoon/maven/"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "net.mtgto" %% "garoon" % "0.2.0-SNAPSHOT",
  "org.webjars" % "bootswatch-united" % "3.3.1+2"
)

dependencyOverrides += "org.apache.ws.commons.axiom" % "axiom-impl" % "1.2.14"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

TwirlKeys.templateImports ++= Seq(
  "views.html.helper._",
  "play.api.i18n.Messages"
)
