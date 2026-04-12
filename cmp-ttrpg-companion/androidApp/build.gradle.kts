import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

// com.android.kotlin.multiplatform.library does not register compose resources as AAR assets
// (variant.sources.assets is null for KotlinMultiplatformAndroidVariant, so
// CopyResourcesToAndroidAssetsTask.outputDirectory is never wired and the AAR has no assets).
// We bypass the broken task and pull compose resources from composeApp's jvmMain assembled
// output, which has the same content (commonMain resources) and the correct directory structure.
val composeResourcesAssetsDir = project(":composeApp").projectDir.resolve(
    "build/generated/compose/resourceGenerator/assembledResources/jvmMain",
)

android {
    namespace = "com.cyrillrx.rpg.android"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        applicationId = "com.cyrillrx.rpg"
        minSdk = Version.MIN_SDK
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

    sourceSets {
        getByName("main") {
            assets.srcDir(composeResourcesAssetsDir)
        }
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

// androidApp is a no-source shell module; exclude it at the Gradle level so the Sonar plugin
// does not try to access AppExtension (removed in AGP 9) during the configuration phase.
sonar {
    isSkipProject = true
}

// Ensure compose resources are assembled before assets are merged into the APK
tasks.configureEach {
    if (name.startsWith("merge") && name.endsWith("Assets")) {
        dependsOn(":composeApp:assembleJvmMainResources")
    }
}
