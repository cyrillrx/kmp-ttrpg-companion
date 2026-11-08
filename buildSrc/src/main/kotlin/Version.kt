import org.gradle.api.JavaVersion

object Version {
    const val APP_VERSION = "1.0.0" // x-release-please-version

    val VERSION_CODE = versionCodeOf(APP_VERSION)

    const val MIN_SDK = 26
    const val COMPILE_SDK = 37

    val java = JavaVersion.VERSION_17
}

private const val PART_CAPACITY = 100
private val SEMANTIC_VERSION_PATTERN = Regex("""(\d+)\.(\d+)\.(\d+)""")

private fun versionCodeOf(version: String): Int {
    val match = requireNotNull(SEMANTIC_VERSION_PATTERN.matchEntire(version)) { "$version: expected MAJOR.MINOR.PATCH" }
    val (major, minor, patch) = match.destructured.toList().map(String::toInt)
    require(minor < PART_CAPACITY && patch < PART_CAPACITY) {
        "$version: minor and patch must stay below $PART_CAPACITY to keep the version code increasing"
    }

    return (major * PART_CAPACITY + minor) * PART_CAPACITY + patch
}
