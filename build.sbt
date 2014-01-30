name := "RestWebApp"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "javanetDeps" at "http://download.java.net/maven/2/"

resolvers += "repo1" at "http://repo1.maven.org/maven2/"

libraryDependencies ++= Seq(
  "com.sun.jersey" % "jersey-core" % "1.13",
	"com.sun.jersey" % "jersey-server" % "1.13",
	"com.sun.jersey" % "jersey-servlet" % "1.13",
  "com.sun.jersey" % "jersey-client" % "1.13",
	"org.glassfish.jersey.test-framework.providers" % "jersey-test-framework-provider-jetty" % "2.5.1",
	"javax.ws.rs" % "jsr311-api" % "1.1.1",
  "org.eclipse.jetty" % "jetty-server" % "9.1.1.v20140108",
  "org.eclipse.jetty" % "jetty-servlet" % "9.1.1.v20140108",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3",
  "com.typesafe.akka" %% "akka-kernel" % "2.2.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.3",
  "org.scalatest" % "scalatest_2.10" % "2.0"
)

