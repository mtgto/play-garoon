name := "play-garoon"

version := "1.0-SNAPSHOT"

resolvers += "github mtgto/garoon" at "http://mtgto.github.io/garoon/maven/"

libraryDependencies ++= Seq(
  "net.mtgto" %% "garoon" % "0.1.0-SNAPSHOT"
)     

play.Project.playScalaSettings

