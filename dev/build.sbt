ThisBuild / scalaVersion := "3.5.0"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"

lazy val root = (project in file("."))
  .settings(
    name := "PListProject",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.18" % Test
    )
  )