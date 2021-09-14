plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(Version.compileSdk)
    buildToolsVersion(Version.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
        applicationId("com.cyrillrx.rpg")
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
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

//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(files("$rootDir/libs/logger.2021.09.14.aar"))
    implementation(files("$rootDir/libs/logger.logcat.2021.09.14.aar"))
    implementation(files("$rootDir/libs/tracker.2021.09.14.aar"))
    implementation(files("$rootDir/libs/notifier.2021.09.14.aar"))
    implementation(files("$rootDir/libs/core.2021.09.14.aar"))
    implementation(files("$rootDir/libs/templates.2021.09.14.aar"))

//    implementation("com.cyrillrx.android:notifier:0.1.2")
//    implementation("com.cyrillrx.android:logger-logcat:1.7")
//    implementation("com.cyrillrx.android:core:0.5.1")
//    implementation("com.cyrillrx.android:ui-templates:0.3.5")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.code.gson:gson:2.8.6")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
