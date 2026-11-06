enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RPG_companion"

include(":ktlint-rules")

include(":app:androidApp")
include(":app:baselineprofile")
include(":app:desktopApp")
include(":app:shared:domain")
include(":app:shared:ui")
