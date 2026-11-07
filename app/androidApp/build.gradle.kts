import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.baselineprofile)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.cyrillrx.rpg.android"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        applicationId = "com.cyrillrx.rpg"
        minSdk = Version.MIN_SDK
        targetSdk = Version.COMPILE_SDK
        versionCode = 1
        versionName = Version.APP_VERSION
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
        freeCompilerArgs.add("-Xexplicit-backing-fields")
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
//
// Source: app/shared/ui/build/generated/compose/resourceGenerator/preparedResources/commonMain/composeResources/
// Destination: app/androidApp/build/generated/cmpResources/assets/composeResources/rpg_companion.app.shared.ui.generated.resources/

// Register the assets source dir at configuration time (static path required by the old SourceSet API).
@Suppress("DEPRECATION")
android.sourceSets.getByName("main").assets.srcDir(
    projectDir.resolve("build/generated/cmpResources/assets"),
)

// Register the copy task and task dependencies after both projects are evaluated.
val copyCmpResourcesToAssets = tasks.register("copyCmpResourcesToAssets", Copy::class.java) {
    dependsOn(":app:shared:ui:prepareComposeResourcesTaskForCommonMain")
    val preparedResources = "generated/compose/resourceGenerator/preparedResources/commonMain/composeResources"
    val resourcesPackage = "rpg_companion.app.shared.ui.generated.resources"
    from(project(":app:shared:ui").layout.buildDirectory.dir(preparedResources))
    into(layout.buildDirectory.dir("generated/cmpResources/assets/composeResources/$resourcesPackage"))
}

// Every variant's asset merge must wait for the Compose resources to be copied into assets,
// including the baseline-profile plugin's benchmarkRelease and nonMinifiedRelease variants.
tasks.matching { it.name.startsWith("merge") && it.name.endsWith("Assets") }
    .configureEach { dependsOn(copyCmpResourcesToAssets) }

ktlint {
    version.set(libs.versions.ktlint.engine)
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
