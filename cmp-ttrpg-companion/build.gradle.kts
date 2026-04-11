plugins {
    // trick: for the same plugin versions in all submodules
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.sonarqube)
}

sonar {
    properties {
        property("sonar.projectKey", "cyrillrx_ttrpg-companion")
        property("sonar.organization", "cyrillrx")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.sources",
            "composeApp/src/commonMain/kotlin," +
                "composeApp/src/jvmMain/kotlin," +
                "shared/core/src/commonMain/kotlin," +
                "shared/core/src/jvmMain/kotlin",
        )
        property(
            "sonar.tests",
            "composeApp/src/commonTest/kotlin," +
                "shared/core/src/commonTest/kotlin",
        )
        property(
            "sonar.java.binaries",
            "composeApp/build/classes/kotlin/jvm/main," +
                "shared/core/build/classes/kotlin/jvm/main",
        )
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "composeApp/build/reports/kover/reportJvm.xml," +
                "shared/core/build/reports/kover/reportJvm.xml",
        )
    }
}
