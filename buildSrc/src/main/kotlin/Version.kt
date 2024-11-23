import org.gradle.api.JavaVersion

object Version {
    const val MIN_SDK = 24
    const val COMPILE_SDK = 34

    val java = JavaVersion.VERSION_17
    const val JVM_TARGET = "17"

    const val compose = "1.1.1"
}
