package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Purple700,
    onPrimary = Color.White,
    primaryContainer = Purple200,
    onPrimaryContainer = Purple700,
    secondary = Red700,
    onSecondary = Color.Black,
    secondaryContainer = Purple300,
    onSecondaryContainer = Purple50,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Red800,
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Purple300,
    onPrimary = Color.White,
    primaryContainer = DarkerGrey,
    onPrimaryContainer = Color.White,
    secondary = Red300,
    onSecondary = Color.White,
    secondaryContainer = Purple200,
    onSecondaryContainer = DarkerGrey,
    background = DarkerGrey,
    onBackground = Color.White,
    surface = DarkGrey,
    onSurface = Color.White,
    error = Red200,
    onError = Color.White,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
