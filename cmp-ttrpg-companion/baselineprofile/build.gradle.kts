import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "com.cyrillrx.rpg.baselineprofile"
    compileSdk = Version.COMPILE_SDK

    defaultConfig {
        // Baseline-profile generation and macrobenchmark require API 28+.
        minSdk = 28
        targetSdk = Version.COMPILE_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // The managed device is an emulator, where absolute frame timings are unreliable. Suppress
        // the guard so ScrollBenchmark runs and yields RELATIVE numbers (None vs BaselineProfile on
        // the same device). Use a physical device for absolute measurements.
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    compileOptions {
        sourceCompatibility = Version.java
        targetCompatibility = Version.java
    }

    // The application module whose journeys we profile / benchmark.
    targetProjectPath = ":androidApp"

    // AOSP image (no Play Store) so the device is rootable for profile capture.
    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel6Api34") {
            device = "Pixel 6"
            apiLevel = 34
            systemImageSource = "aosp"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(Version.java.majorVersion))
    }
}

// Run generation/benchmarks on the managed device by default (no physical device needed).
baselineProfile {
    managedDevices += "pixel6Api34"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
