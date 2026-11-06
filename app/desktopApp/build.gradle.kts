import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }
}

dependencies {
    implementation(projects.app.shared.ui)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.swing)

    ktlintRuleset(projects.ktlintRules)
}

compose.desktop {
    application {
        mainClass = "com.cyrillrx.rpg.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cyrillrx.rpg"
            packageVersion = Version.APP_VERSION
            modules("java.instrument", "java.sql", "jdk.unsupported")
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
            optimize.set(false)
        }
    }
}

ktlint {
    version.set(libs.versions.ktlint.engine)
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
