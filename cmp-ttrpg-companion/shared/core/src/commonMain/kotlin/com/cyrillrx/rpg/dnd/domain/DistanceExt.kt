package com.cyrillrx.rpg.dnd.domain

const val DND_FEET_STEP = 5
internal const val DND_METERS_STEP = 1.5f

fun Int.feetToMeters(): Float = this / DND_FEET_STEP.toFloat() * DND_METERS_STEP

// [min] and [max] are expected to sit on the grid themselves: the final clamp returns a bound as is,
// off the grid if the bound is. The rounding goes through Long so that no bound can overflow the
// multiplication onto the opposite bound.
fun Int.coerceToNearestFootStep(min: Int, max: Int): Int {
    val clamped = coerceIn(min, max).toLong()
    val rounded = (clamped + DND_FEET_STEP / 2).floorDiv(DND_FEET_STEP) * DND_FEET_STEP
    return rounded.coerceIn(min.toLong(), max.toLong()).toInt()
}
