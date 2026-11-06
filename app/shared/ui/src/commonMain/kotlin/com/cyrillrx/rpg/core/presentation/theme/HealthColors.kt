package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HealthColors(
    val heal: Color,
    val healContainer: Color,
    val warning: Color,
    val danger: Color,
)

val healthColorsLight = HealthColors(
    heal = HealLight,
    healContainer = HealContainerLight,
    warning = WarningLight,
    danger = DangerLight,
)

val healthColorsDark = HealthColors(
    heal = HealDark,
    healContainer = HealContainerDark,
    warning = WarningDark,
    danger = DangerDark,
)

val LocalHealthColors = staticCompositionLocalOf { healthColorsLight }
