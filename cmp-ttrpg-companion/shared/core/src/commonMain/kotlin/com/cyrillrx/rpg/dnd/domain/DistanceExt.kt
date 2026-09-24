package com.cyrillrx.rpg.dnd.domain

import kotlin.math.roundToInt

const val DND_FEET_STEP = 5
internal const val DND_METERS_STEP = 1.5f

fun Int.feetToMeters(): Float = this / DND_FEET_STEP.toFloat() * DND_METERS_STEP

// Rounding before clamping keeps the result on the grid; clamping first could land between two
// squares. [min] and [max] are expected to sit on the grid themselves.
fun Int.coerceToNearestFootStep(min: Int, max: Int): Int {
    val rounded = (toFloat() / DND_FEET_STEP).roundToInt() * DND_FEET_STEP
    return rounded.coerceIn(min, max)
}
