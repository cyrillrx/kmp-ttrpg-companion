package com.cyrillrx.core.data

import java.io.File
import kotlin.test.fail

private const val RESOURCES_PATH = "composeApp/src/commonMain/composeResources/files"

// Walking up rather than resolving a fixed path: the resources live in composeApp, which has no JVM
// test source set of its own.
fun shippedDataFile(name: String): File {
    val relativePath = "$RESOURCES_PATH/$name"
    var directory: File? = File(".").absoluteFile
    while (directory != null) {
        val candidate = File(directory, relativePath)
        if (candidate.isFile) return candidate
        directory = directory.parentFile
    }
    fail("could not find $relativePath above ${File(".").absolutePath}")
}
