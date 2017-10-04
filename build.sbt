
name := """Zoop"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  jdbc,
  javaWs
)

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.33",
  "com.amazonaws" % "aws-java-sdk" % "1.3.11",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.mongodb" % "mongo-java-driver" % "2.12.2",
  "org.mongodb.morphia" % "morphia" % "1.0.0",
  "org.mongodb.morphia" % "morphia-logging-slf4j" % "1.0.0",
  "org.mongodb.morphia" % "morphia-validation" % "1.0.0",
  "org.apache.commons" % "commons-email" % "1.2",
  "org.imgscalr" % "imgscalr-lib" % "4.2"
  //"com.sun.media" % "jai-codec" % "1.1.3",
  //"org.im4java" % "im4java" % "1.4.0"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := false