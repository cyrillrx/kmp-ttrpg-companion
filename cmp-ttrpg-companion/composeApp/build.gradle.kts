import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_reports")
    metricsDestination = layout.buildDirectory.dir("compose_metrics")
    stabilityConfigurationFile = rootProject.file("compose_stability.conf")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.addAll("-Xexplicit-backing-fields")
    }

    android {
        namespace = "com.cyrillrx.rpg"
        compileSdk = Version.COMPILE_SDK
        minSdk = Version.MIN_SDK
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.java.majorVersion))
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
        commonMain.dependencies {
            implementation(projects.shared.core)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material) // Added for Icons
            implementation(compose.materialIconsExtended)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.mikepenz.markdown.m3)

            implementation(libs.coil3.compose)
            implementation(libs.coil3.svg)

            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.ui)
            implementation(libs.androidx.ui.tooling)

            implementation(libs.legacy.material)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cyrillrx.rpg"
            packageVersion = "1.0.0"
        }
    }
}

sonar {
    properties {
        // Absolute: the report is then found whatever base directory Sonar resolves against.
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/kover/reportJvm.xml").get().asFile.absolutePath,
        )
        // Sonar indexes these files either way and reads their absence from the report as zero
        // coverage. See the coverage policy in AGENTS.md.
        property(
            "sonar.coverage.exclusions",
            listOf(
                "**/presentation/component/**",
                "**/presentation/theme/**",
                "**/navigation/**",
                "**/*Screen.kt",
                "**/app/**",
                "**/androidMain/**",
                "**/iosMain/**",
            ).joinToString(","),
        )
    }
}

kover {
    reports {
        filters {
            // Coverage only comes from jvmTest and no Compose UI test feeds Kover, so measuring
            // composables would only count tests that are never collected.
            excludes {
                classes(
                    "*.presentation.component.*",
                    "*.presentation.theme.*",
                    // Route declarations, mostly kotlinx.serialization generated members.
                    "*.navigation.*",
                    "*.ComposableSingletons*",
                    "*Screen",
                    "*ScreenKt",
                    // Navigation root, plus locale constants and an expect declaration.
                    "*.app.*",
                    // Generated: Compose resources accessors.
                    "*.generated.resources.*",
                )
            }
        }
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(true)
}
