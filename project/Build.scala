import sbt._
import Keys._

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

object ApplicationBuild extends Build {

  lazy val formattingPreferences = FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test := formattingPreferences)

  val buildSettings = Defaults.coreDefaultSettings ++ Publish.defaultSettings ++ formatSettings ++ Seq(
    scalaVersion in ThisBuild := Version.scalaVersion,
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(_ :: Nil),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(_ :: Nil),
    scalacOptions in Compile ++= List("-Xmax-classfile-name", "143"), // ecryptfs limit, see https://bugs.launchpad.net/ecryptfs/+bug/344878 and https://issues.scala-lang.org/browse/SI-3623
    scalacOptions in Compile += "-feature",
    scalacOptions += "-Dscalac.patmat.analysisBudget=off",
    resolvers := Seq(
      Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
      "Zitadelz Snapshots" at "https://gitlab.com/nzf/artifactory/raw/master/snapshots/",
      "Zitadelz Artifactory" at "http://artifactory/artifactory/repo",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
      "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases",
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      Resolver.url("Zitadelz Artifactory Ivy", url("http://artifactory:8081/artifactory/repo"))(Resolver.ivyStylePatterns))) ++ super.settings

  lazy val root = Project(id = "root", base = file("."), settings = buildSettings)
}
