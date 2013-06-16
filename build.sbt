scalaVersion := "2.10.2"

mainClass in Compile := Some("Main")

resolvers ++= Seq(
  "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
)

libraryDependencies ++= Seq(
  "play" %% "templates" % "2.1.1"
)
