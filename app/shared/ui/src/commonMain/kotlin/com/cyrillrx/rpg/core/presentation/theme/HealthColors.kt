package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Palette-agnostic: a healthy pool and healing read green and a dwindling pool reads amber whatever palette the
// player picked. Following colorScheme.primary for the healthy segment would tie it to the palette: the Dragon one is
// red by construction, which left a full bar and a critical one the same colour.
// A critical pool fills with Danger rather than colorScheme.error: the Material dark error tone is a pale salmon
// meant for text drawn on a surface, and a bar filled with it reads no redder than the amber it replaces.
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
