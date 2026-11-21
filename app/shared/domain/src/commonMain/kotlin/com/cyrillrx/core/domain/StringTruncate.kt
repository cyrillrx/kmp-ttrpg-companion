package com.cyrillrx.core.domain

fun String.truncate(maxLength: Int): String {
    val truncated = take(maxLength)
    return if (truncated.lastOrNull()?.isHighSurrogate() == true) truncated.dropLast(1) else truncated
}
