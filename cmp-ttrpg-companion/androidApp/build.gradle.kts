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
