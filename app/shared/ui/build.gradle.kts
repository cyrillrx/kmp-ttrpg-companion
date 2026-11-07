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

// Common code has no BuildConfig: the version an exported file records is generated from the build.
val generateAppVersion by tasks.registering {
    val appVersion = Version.APP_VERSION
    val outputDir = layout.buildDirectory.dir("generated/appVersion/commonMain/kotlin")
    inputs.property("appVersion", appVersion)
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("com/cyrillrx/rpg/app/AppVersion.kt").asFile
        file.parentFile.mkdirs()
        file.writeText("package com.cyrillrx.rpg.app\n\nconst val APP_VERSION = \"$appVersion\"\n")
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_reports")
    metricsDestination = layout.buildDirectory.dir("compose_metrics")
    stabilityConfigurationFile = project.file("compose_stability.conf")
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
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            binaryOption("bundleId", "com.cyrillrx.rpg.ui")
        }
    }

    jvm()

    sourceSets {
        commonMain {
            kotlin.srcDir(generateAppVersion)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        commonMain.dependencies {
            api(projects.app.shared.domain)

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
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

kover {
    currentProject {
        createVariant("jvmOnly") {
            add("jvm")
        }
    }
}

sonar {
    properties {
        // One aggregated report for the whole build: the tests that exercise a type do not always
        // live in its module. Absolute, so it is found whatever base directory Sonar resolves
        // against; each module reads the same file and finds only its own sources in it.
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            rootProject.layout.buildDirectory.file("reports/kover/reportJvmOnly.xml").get().asFile.absolutePath,
        )
        // Sonar indexes these files either way and reads their absence from the report as zero
        // coverage. Every entry here has its counterpart in the root kover block: Kover matches
        // class names, Sonar matches file paths. See the coverage policy in AGENTS.md.
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

ktlint {
    version.set(libs.versions.ktlint.engine)
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

dependencies {
    ktlintRuleset(projects.ktlintRules)
}
