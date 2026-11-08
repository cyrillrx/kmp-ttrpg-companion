import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.compose.compiler)
}

fun signingProperty(name: String): String? = providers.gradleProperty(name).orNull

val releaseStoreFile = signingProperty("ttrpgReleaseStoreFile")
val missingSigningProperties = if (releaseStoreFile == null) {
    emptyList()
} else {
    listOf("ttrpgReleaseStorePassword", "ttrpgReleaseKeyAlias", "ttrpgReleaseKeyPassword")
        .filter { signingProperty(it) == null }
}

android {
    namespace = "com.cyrillrx.rpg.android"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        applicationId = "com.cyrillrx.rpg"
        minSdk = Version.MIN_SDK
        targetSdk = Version.COMPILE_SDK
        versionCode = Version.VERSION_CODE
        versionName = Version.APP_VERSION
    }

    signingConfigs {
        if (releaseStoreFile != null && missingSigningProperties.isEmpty()) {
            create("release") {
                storeFile = file(releaseStoreFile)
                storePassword = signingProperty("ttrpgReleaseStorePassword")
                keyAlias = signingProperty("ttrpgReleaseKeyAlias")
                keyPassword = signingProperty("ttrpgReleaseKeyPassword")
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        getByName("release") {
            // TODO(#395): give R8 the default and app rules files
            isMinifyEnabled = true
            signingConfig = signingConfigs.findByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = Version.java
        targetCompatibility = Version.java
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(Version.java.majorVersion))
    }
}

dependencies {
    implementation(projects.app.shared.domain)
    implementation(projects.app.shared.ui)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.profileinstaller)
    debugImplementation(libs.androidx.ui.tooling)
    baselineProfile(projects.app.baselineprofile)

    ktlintRuleset(projects.ktlintRules)
}

// Workaround: com.android.kotlin.multiplatform.library does not bundle Compose Resources into
// the AAR, so androidApp copies them from the ui module's prepared resources output.
// The CMP task copyAndroidMainComposeResourcesToAndroidAssets has no outputDirectory configured
// for this plugin variant. See: https://youtrack.jetbrains.com/issue/CMP-7877

// Register the assets source dir at configuration time (static path required by the old SourceSet API).
@Suppress("DEPRECATION")
android.sourceSets.getByName("main").assets.srcDir(
    projectDir.resolve("build/generated/cmpResources/assets"),
)

val copyCmpResourcesToAssets = tasks.register("copyCmpResourcesToAssets", Copy::class.java) {
    dependsOn(":app:shared:ui:prepareComposeResourcesTaskForCommonMain")
    val preparedResources = "generated/compose/resourceGenerator/preparedResources/commonMain/composeResources"
    // TODO(#374): read the package the ui module pins instead of hardcoding it
    val resourcesPackage = "rpg_companion.app.shared.ui.generated.resources"
    from(project(":app:shared:ui").layout.buildDirectory.dir(preparedResources))
    into(layout.buildDirectory.dir("generated/cmpResources/assets/composeResources/$resourcesPackage"))
}

// Every variant's asset merge must wait for the Compose resources to be copied into assets,
// including the baseline-profile plugin's benchmarkRelease and nonMinifiedRelease variants.
tasks.named { it.startsWith("merge") && it.endsWith("Assets") }
    .configureEach { dependsOn(copyCmpResourcesToAssets) }

// TODO(#394): register the copied resources as a generated assets source instead of wiring consumers by name
tasks.named { it.startsWith("lint") || it.contains("Lint") }
    .configureEach { dependsOn(copyCmpResourcesToAssets) }

tasks.named { it == "packageRelease" || it == "signReleaseBundle" }.configureEach {
    val missing = missingSigningProperties
    inputs.property("missingSigningProperties", missing)
    doFirst {
        check(missing.isEmpty()) { "Release signing needs the Gradle properties ${missing.joinToString()}" }
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
