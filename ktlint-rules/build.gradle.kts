import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        // The rules run inside ktlint, on the Kotlin stdlib it ships.
        // TODO(#402): run the tests on that stdlib too
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
    }
}

dependencies {
    compileOnly(libs.ktlint.cli.ruleset.core)
    compileOnly(libs.ktlint.rule.engine.core)

    testImplementation(libs.ktlint.cli.ruleset.core)
    testImplementation(libs.ktlint.rule.engine.core)
    testImplementation(libs.ktlint.test)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.slf4j.simple)
}

tasks.test {
    useJUnitPlatform()
}

ktlint {
    version.set(libs.versions.ktlint.engine)
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
