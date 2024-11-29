package com.cyrillrx.rpg.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Purple700,
//    primaryVariant = Purple900,
    secondary = Red700,
//    secondaryVariant = Red900,
    background = Color.White,
    surface = Color.White,
    error = Red800,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Red300,
//    primaryVariant = Red700,
    secondary = Red300,
//    secondaryVariant = Red900,
    background = darkerGrey,
    surface = darkGrey,
    error = Red200,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
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
