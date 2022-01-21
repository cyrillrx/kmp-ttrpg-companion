plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = Version.compileSdk

    defaultConfig {
        minSdk = Version.minSdk
        targetSdk = Version.targetSdk

        applicationId = "com.cyrillrx.rpg"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kotlinOptions {
        jvmTarget = Version.java
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Version.kotlin
        kotlinCompilerExtensionVersion = Version.composeCompiler
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}")

    implementation(project(":model"))

    implementation(files("$rootDir/libs/logger.2021.09.14.aar"))
    implementation(files("$rootDir/libs/logger.logcat.2021.09.14.aar"))
    implementation(files("$rootDir/libs/tracker.2021.09.14.aar"))
    implementation(files("$rootDir/libs/notifier.2021.09.14.aar"))
    implementation(files("$rootDir/libs/core.2021.09.14.aar"))
    implementation(files("$rootDir/libs/templates.2021.09.14.aar"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("androidx.compose.ui:ui:${Version.compose}")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.material:material:${Version.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.compose}")

    implementation("com.google.android.material:material:1.5.0")
    implementation("com.google.code.gson:gson:2.8.8")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
