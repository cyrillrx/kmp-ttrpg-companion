package com.cyrillrx.core.domain

/** May return `maxLength - 1` characters rather than split a surrogate pair. */
fun String.truncate(maxLength: Int): String {
    val truncated = take(maxLength)
    return if (truncated.lastOrNull()?.isHighSurrogate() == true) truncated.dropLast(1) else truncated
}
