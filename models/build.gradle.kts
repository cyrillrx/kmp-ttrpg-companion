plugins {
    `kotlin-dsl`
    `java-library`
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.50"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}