import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.cyrillrx.rpg.android"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        applicationId = "com.cyrillrx.rpg"
        minSdk = Version.MIN_SDK
        targetSdk = Version.COMPILE_SDK
        versionCode = 1
        versionName = "1.0"
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
    }
}

dependencies {
    implementation(projects.composeApp)
    debugImplementation(libs.androidx.ui.tooling)
}

// Workaround: com.android.kotlin.multiplatform.library does not bundle Compose Resources into
// the AAR, so androidApp copies them from composeApp's prepared resources output.
// The CMP task copyAndroidMainComposeResourcesToAndroidAssets has no outputDirectory configured
// for this plugin variant. See: https://youtrack.jetbrains.com/issue/CMP-7877
//
// Source: composeApp/build/generated/compose/resourceGenerator/preparedResources/commonMain/composeResources/
// Destination: androidApp/build/generated/cmpResources/assets/composeResources/rpg_companion.composeapp.generated.resources/

// Register the assets source dir at configuration time (static path required by the old SourceSet API).
@Suppress("DEPRECATION")
android.sourceSets.getByName("main").assets.srcDir(
    projectDir.resolve("build/generated/cmpResources/assets"),
)

// Register the copy task and task dependencies after both projects are evaluated.
val copyCmpResourcesToAssets = tasks.register("copyCmpResourcesToAssets", Copy::class.java) {
    dependsOn(":composeApp:prepareComposeResourcesTaskForCommonMain")
    from(project(":composeApp").layout.buildDirectory.dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources"))
    into(layout.buildDirectory.dir("generated/cmpResources/assets/composeResources/rpg_companion.composeapp.generated.resources"))
}

afterEvaluate {
    tasks.named("mergeDebugAssets") { dependsOn(copyCmpResourcesToAssets) }
    tasks.named("mergeReleaseAssets") { dependsOn(copyCmpResourcesToAssets) }
}
