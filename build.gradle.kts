import org.sonarqube.gradle.SonarExtension

plugins {
    // trick: for the same plugin versions in all submodules
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.sonarqube)
}

// The application shells have no analyzable code and trip a scanner/AGP 9 incompatibility
// (sonarResolver queries res providers before their producing task runs).
// The ktlint rules are build tooling, outside the app the quality gate measures.
listOf(":app:androidApp", ":app:baselineprofile", ":app:desktopApp", ":ktlint-rules").forEach { path ->
    project(path) {
        extensions.configure<SonarExtension>("sonar") {
            isSkipProject = true
        }
    }
}

kover {
    currentProject {
        // Merges the modules' jvmOnly variants: coverage comes from jvmTest alone, and the
        // default total variant would also compile and report the Android target.
        createVariant("jvmOnly") {}
    }
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
                    // Nested: the lambdas of a screen compile into ScreenKt$Screen$1$1 and
                    // the exact-match pattern above does not reach them.
                    "*ScreenKt$*",
                    // Navigation root, plus locale constants and an expect declaration.
                    "*.app.*",
                    // Generated: Compose resources accessors.
                    "*.generated.resources.*",
                    // Generated: SQLDelight database, queries and row types.
                    "com.cyrillrx.rpg.cache.*",
                )
            }
        }
    }
}

dependencies {
    kover(projects.app.shared.domain)
    kover(projects.app.shared.ui)
}

sonar {
    properties {
        property("sonar.projectKey", "cyrillrx_ttrpg-companion_client-cmp")
        property("sonar.organization", "cyrillrx")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
