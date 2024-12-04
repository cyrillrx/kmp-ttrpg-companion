package com.cyrillrx.rpg.common.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun HtmlText(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier)
}
