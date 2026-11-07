package com.cyrillrx.rpg.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val default = Typography()

/**
 * App type scale, built on the Material 3 defaults (default font family).
 * The `title*` roles carry their own weight/size so components no longer
 * re-specify `fontSize`/`fontWeight` inline.
 */
val Typography = default.copy(
    titleLarge = default.titleLarge.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMedium = default.titleMedium.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleSmall = default.titleSmall.copy(
        fontWeight = FontWeight.Bold,
    ),
)
