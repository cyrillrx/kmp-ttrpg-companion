package com.cyrillrx.rpg.core.presentation

import androidx.compose.runtime.compositionLocalOf
import com.cyrillrx.rpg.settings.domain.DistanceUnit

val LocalDistanceUnit = compositionLocalOf { DistanceUnit.FEET }
