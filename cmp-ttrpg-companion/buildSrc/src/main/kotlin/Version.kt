import org.gradle.api.JavaVersion

object Version {
    const val APP_VERSION = "1.0.0"

    const val MIN_SDK = 26
    const val COMPILE_SDK = 36

    val java = JavaVersion.VERSION_17
}
