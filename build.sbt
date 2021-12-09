val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "adventofcode",
    version := "1.0.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.10",
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "org.scalatest" %% "scalatest" % "3.2.10"
    )
  )
