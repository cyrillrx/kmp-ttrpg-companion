package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

const val SUBTITLE_SEPARATOR = " · "

fun joinNonNull(vararg segments: String?): String? {
    val validSegments = segments.filterNotNull()
    if (validSegments.isEmpty()) return null

    return validSegments.joinToString("")
}

internal fun buildSubtitle(
    type: String,
    typeColor: Color,
    subtitle: String?,
    subtitleColor: Color,
): AnnotatedString = buildAnnotatedString {
    withStyle(SpanStyle(color = typeColor)) { append(type) }
    subtitle?.let {
        withStyle(SpanStyle(color = subtitleColor)) { append(subtitle) }
    }
}
