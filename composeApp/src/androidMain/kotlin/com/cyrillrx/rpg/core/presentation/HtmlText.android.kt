package com.cyrillrx.rpg.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun HtmlText(text: String, modifier: Modifier) {
    AndroidHtmlText(text = text, modifier = modifier)
}
