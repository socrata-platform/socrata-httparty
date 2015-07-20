name := "HttParty"

resolvers ++= Seq(
  "socrata maven" at "https://repository-socrata-oss.forge.cloudbees.com/release",
  "socrata maven-snap" at "https://repository-socrata-oss.forge.cloudbees.com/snapshot")

libraryDependencies ++= Seq(
  "com.socrata" %% "socrata-http-jetty" % "2.0.0-SNAPSHOT",
  "com.socrata" %% "socrata-http-curator-broker" % "2.0.0-SNAPSHOT",
  "com.netflix.curator" % "curator-x-discovery" % "1.3.3",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "optional",
   "com.rojoma" %% "rojoma-json" % "[2.0.0,3.0.0)",
   "com.rojoma" %% "simple-arm" % "[1.1.10,2.0.0)"
  )

scalacOptions ++= Seq("-optimize","-deprecation","-feature")
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
// TODO: enable scalastyle build failures
com.socrata.sbtplugins.StylePlugin.StyleKeys.styleFailOnError in Compile := false
// TODO: enable code coverage build failures
scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := false
