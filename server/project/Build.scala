import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "server"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "guillaume.bort" % "cagette_2.9.1" % "0.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Cagette Resolver
    resolvers += "Guillaume Bort" at "http://guillaume.bort.fr/repository"  
  )

}
