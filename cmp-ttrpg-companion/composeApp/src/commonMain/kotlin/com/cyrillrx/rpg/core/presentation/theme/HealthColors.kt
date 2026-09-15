package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HealthColors(
    val heal: Color,
    val healContainer: Color,
)

val healthColorsLight = HealthColors(
    heal = HealLight,
    healContainer = HealContainerLight,
)

val healthColorsDark = HealthColors(
    heal = HealDark,
    healContainer = HealContainerDark,
)

val LocalHealthColors = staticCompositionLocalOf { healthColorsLight }
