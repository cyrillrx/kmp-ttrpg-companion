package com.cyrillrx.core.domain

/** Never splits an emoji in two, so the result may be one character shorter than [maxLength]. */
fun String.truncate(maxLength: Int): String {
    val truncated = take(maxLength)
    return if (truncated.lastOrNull()?.isHighSurrogate() == true) truncated.dropLast(1) else truncated
}
