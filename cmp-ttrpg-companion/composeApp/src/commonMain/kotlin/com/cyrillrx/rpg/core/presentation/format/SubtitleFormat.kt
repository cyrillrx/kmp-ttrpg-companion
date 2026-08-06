package com.cyrillrx.rpg.core.presentation.format

const val SUBTITLE_SEPARATOR = " · "

fun joinNonNull(vararg segments: String?): String? {
    val validSegments = segments.filterNotNull()
    if (validSegments.isEmpty()) return null

    return validSegments.joinToString("")
}
