package com.cyrillrx.rpg.dnd.domain

import kotlin.math.roundToInt

const val DND_FEET_STEP = 5
internal const val DND_METERS_STEP = 1.5f

fun Int.feetToMeters(): Float = this / DND_FEET_STEP.toFloat() * DND_METERS_STEP

// Clamping first keeps the multiplication away from Int.MAX_VALUE, where it would overflow and land
// on the opposite bound.
fun Int.coerceToNearestFootStep(min: Int, max: Int): Int {
    val clamped = coerceIn(min, max)
    val rounded = (clamped.toFloat() / DND_FEET_STEP).roundToInt() * DND_FEET_STEP
    return rounded.coerceIn(min, max)
}
