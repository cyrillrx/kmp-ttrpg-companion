package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HealthColors(
    val high: Color,
    val medium: Color,
    val low: Color,
    val temporary: Color,
)

val healthColorsLight = HealthColors(
    high = HealthHighLight,
    medium = HealthMediumLight,
    low = ErrorLight,
    temporary = TemporaryHitPointsLight,
)

val healthColorsDark = HealthColors(
    high = HealthHighDark,
    medium = HealthMediumDark,
    low = ErrorDark,
    temporary = TemporaryHitPointsDark,
)

val LocalHealthColors = staticCompositionLocalOf { healthColorsLight }
