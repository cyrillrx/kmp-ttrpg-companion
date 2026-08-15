import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kover)
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.addAll("-Xexplicit-backing-fields")
    }

    android {
        namespace = "com.cyrillrx.rpg.core"
        compileSdk = Version.COMPILE_SDK
        minSdk = Version.MIN_SDK
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Version.java.majorVersion))
        }
    }

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Core"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.serialization.core)
            api(libs.kotlinx.serialization.json)

            implementation(libs.sqldelight.runtime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native)
        }
        jvmMain.dependencies {
            implementation(libs.sqldelight.jvm)
        }
        jvmTest.dependencies {
            implementation(libs.sqldelight.jvm)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.cyrillrx.rpg.cache")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            // Fails the build when the .sq schema changes without a matching .sqm migration.
            verifyMigrations.set(true)
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                // Generated: SQLDelight database, queries and row types.
                classes("com.cyrillrx.rpg.cache.*")
            }
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
                "**/androidMain/**",
                "**/iosMain/**",
            ).joinToString(","),
        )
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
