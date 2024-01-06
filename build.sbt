import org.scalajs.linker.interface.ModuleSplitStyle

val monocleVersion = "3.2.0"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name         := "ti4",
    scalaVersion := "3.3.1",
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature", "-no-indent"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("example")))
    },
    libraryDependencies ++= Seq(
      "org.scala-js"    %%% "scalajs-dom"   % "2.4.0",
      "io.indigoengine" %%% "tyrian-io"     % "0.8.0",
      "dev.optics"      %%% "monocle-core"  % monocleVersion,
      "dev.optics"      %%% "monocle-macro" % monocleVersion,
    )
  )
