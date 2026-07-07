package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * A theme palette: a self-contained color identity as a light/dark [ColorScheme]
 * pair. Raw color literals belong here rather than in component code.
 */
enum class AppPalette(
    val light: ColorScheme,
    val dark: ColorScheme,
) {
    PURPLE(light = PurpleLightScheme, dark = PurpleDarkScheme),
}

private val PurpleLightScheme = lightColorScheme(
    primary = Color(0xFF512DA8),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE9DDFF),
    onPrimaryContainer = Color(0xFF21005E),
    inversePrimary = Color(0xFFD0BCFF),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1E192B),
    tertiary = Color(0xFF7A5900),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDF9C),
    onTertiaryContainer = Color(0xFF261A00),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1C1B1E),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1C1B1E),
    surfaceVariant = Color(0xFFE7E0EB),
    onSurfaceVariant = Color(0xFF49454E),
    outline = Color(0xFF7A757F),
    outlineVariant = Color(0xFFCBC4CF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF6F0F7),
    surfaceContainer = Color(0xFFF1EBF3),
    surfaceContainerHigh = Color(0xFFEBE5ED),
    surfaceContainerHighest = Color(0xFFE5E0E8),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFF512DA8),
)

private val PurpleDarkScheme = darkColorScheme(
    primary = Color(0xFFB69DF8),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378A),
    onPrimaryContainer = Color(0xFFE9DDFF),
    inversePrimary = Color(0xFF512DA8),
    secondary = Color(0xFFD3C1EC),
    onSecondary = Color(0xFF382C4B),
    secondaryContainer = Color(0xFF4C4160),
    onSecondaryContainer = Color(0xFFEBDCFB),
    tertiary = Color(0xFFF5BD48),
    onTertiary = Color(0xFF402D00),
    tertiaryContainer = Color(0xFF5C4300),
    onTertiaryContainer = Color(0xFFFFDF9C),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF201D26),
    onBackground = Color(0xFFE8E2EA),
    surface = Color(0xFF201D26),
    onSurface = Color(0xFFE8E2EA),
    surfaceVariant = Color(0xFF4A4550),
    onSurfaceVariant = Color(0xFFCCC5D1),
    surfaceContainerLowest = Color(0xFF16131B),
    surfaceContainerLow = Color(0xFF2A2733),
    surfaceContainer = Color(0xFF302C39),
    surfaceContainerHigh = Color(0xFF3B3744),
    surfaceContainerHighest = Color(0xFF46424F),
    outline = Color(0xFF978F9D),
    outlineVariant = Color(0xFF4A4550),
    inverseSurface = Color(0xFFE8E2EA),
    inverseOnSurface = Color(0xFF313033),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFFB69DF8),
)
