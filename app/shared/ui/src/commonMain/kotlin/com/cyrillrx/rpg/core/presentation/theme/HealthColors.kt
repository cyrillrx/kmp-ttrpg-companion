package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Palette-agnostic: green, amber and red whatever palette the player picked. Following colorScheme.primary would tie
// them to the palette, and a red primary makes the best state and the worst one the same colour.
// `danger` is not colorScheme.error: the Material dark error tone is a pale salmon meant for text drawn on a surface,
// and as a fill it reads no redder than `warning`.
data class HealthColors(
    val heal: Color,
    val healContainer: Color,
    val warning: Color,
    val danger: Color,
)

internal val healthColorsLight = HealthColors(
    heal = Color(0xFF2E7D32),
    healContainer = Color(0xFFC8E6C9),
    warning = Color(0xFFE65100),
    danger = Color(0xFFC62828),
)

internal val healthColorsDark = HealthColors(
    heal = Color(0xFF81C784),
    healContainer = Color(0xFF1B5E20),
    warning = Color(0xFFFFB74D),
    danger = Color(0xFFE57373),
)

val LocalHealthColors = staticCompositionLocalOf { healthColorsLight }
