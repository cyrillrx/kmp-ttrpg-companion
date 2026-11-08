import org.gradle.api.JavaVersion

object Version {
    const val APP_VERSION = "1.0.0"

    val VERSION_CODE = versionCodeOf(APP_VERSION)

    const val MIN_SDK = 26
    const val COMPILE_SDK = 37

    val java = JavaVersion.VERSION_17
}

private const val PART_CAPACITY = 100

private fun versionCodeOf(version: String): Int {
    val (major, minor, patch) = version.split(".").map(String::toInt)
    require(minor < PART_CAPACITY && patch < PART_CAPACITY) {
        "$version: minor and patch must stay below $PART_CAPACITY to keep the version code increasing"
    }
    return (major * PART_CAPACITY + minor) * PART_CAPACITY + patch
}
