package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

const val SUBTITLE_SEPARATOR = " · "

@Composable
fun SubtitleSeparator(modifier: Modifier = Modifier) {
    Text(
        text = SUBTITLE_SEPARATOR,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}
