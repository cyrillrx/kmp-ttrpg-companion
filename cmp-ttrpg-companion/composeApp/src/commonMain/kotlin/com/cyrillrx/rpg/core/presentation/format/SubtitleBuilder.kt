package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

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
