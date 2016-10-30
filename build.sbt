organization := "com.socrata"

version := "0.0.1"

name := "HttParty"

scalaVersion := "2.10.3"

resolvers ++= Seq(
  "socrata maven" at "https://repo.socrata.com/artifactory/libs-release",
  "socrata maven-snap" at "https://repo.socrata.com/artifactory/libs-snapshot")


libraryDependencies ++= Seq(
  "com.socrata" %% "socrata-http-jetty" % "2.0.0-SNAPSHOT",
  "com.socrata" %% "socrata-http-curator-broker" % "2.0.0-SNAPSHOT",
  "com.netflix.curator" % "curator-x-discovery" % "1.3.3",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "optional",
   "com.rojoma" %% "rojoma-json" % "[2.0.0,3.0.0)",
   "com.rojoma" %% "simple-arm" % "[1.1.10,2.0.0)"
  )


testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")


scalacOptions ++= Seq("-optimize","-deprecation","-feature")
