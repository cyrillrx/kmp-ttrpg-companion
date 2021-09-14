// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("com.android.tools.build:gradle:4.2.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
