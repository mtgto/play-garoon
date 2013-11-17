name := "play-garoon"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
)     

play.Project.playScalaSettings

lazy val root = project.in(file(".")).aggregate(core).dependsOn(core)

lazy val core = project

