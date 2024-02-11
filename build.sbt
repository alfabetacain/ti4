import org.scalajs.linker.interface.ModuleSplitStyle

val monocleVersion = "3.2.0"
val munitVersion   = "1.0.0-M11"
val scala3Version  = "3.3.1"

lazy val domain = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("domain"))
  .settings(
    name         := "ti4-domain",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature", "-no-indent"),
    libraryDependencies ++= Seq(
      "org.typelevel"   %% "cats-effect"      % "3.5.3",
      "dev.optics"     %%% "monocle-core"     % monocleVersion,
      "dev.optics"     %%% "monocle-macro"    % monocleVersion,
      "co.fs2"         %%% "fs2-core"         % "3.9.4",
      "org.scalameta"  %%% "munit"            % munitVersion % Test,
      "org.scalameta"  %%% "munit-scalacheck" % munitVersion % Test,
      "org.scalacheck" %%% "scalacheck"       % "1.17.0"     % Test,
    )
  )
  .jvmSettings(
    // Add JVM-specific settings here
  )
  .jsSettings(
    // Add JS-specific settings here
    scalaJSUseMainModuleInitializer := true,
  )

lazy val web = project
  .in(file("web"))
  .dependsOn(domain.js)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name         := "ti4-web",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature", "-no-indent"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("example")))
    },
    libraryDependencies ++= Seq(
      "org.scala-js"    %%% "scalajs-dom" % "2.4.0",
      "io.indigoengine" %%% "tyrian-io"   % "0.8.0"
    )
  )

lazy val root = project
  .in(file("."))
  .aggregate(domain.jvm, domain.js, web)
  .settings(
    publish      := {},
    publishLocal := {},
  )
