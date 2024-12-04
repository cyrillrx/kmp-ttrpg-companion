package com.cyrillrx.rpg.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun HtmlText(text: String, modifier: Modifier) {
    AndroidHtmlText(text = text, modifier = modifier)
}
