name := "InitialProject"

version := "0.1"

scalaVersion := "2.12.8"
lazy val hello = (project in file("."))
  .settings(
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  )