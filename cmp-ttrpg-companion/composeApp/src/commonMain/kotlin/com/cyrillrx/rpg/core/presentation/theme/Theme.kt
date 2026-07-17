package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme

@Composable
fun AppTheme(
    theme: Theme,
    palette: Palette = Palette.ARCANE,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = palette.colorScheme(theme),
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

@Composable
fun AppThemePreview(
    darkTheme: Boolean,
    palette: Palette = Palette.ARCANE,
    content: @Composable () -> Unit,
) {
    AppTheme(
        theme = if (darkTheme) Theme.DARK else Theme.LIGHT,
        palette = palette,
        content = {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(spacingMedium),
            ) { content() }
        },
    )
}
