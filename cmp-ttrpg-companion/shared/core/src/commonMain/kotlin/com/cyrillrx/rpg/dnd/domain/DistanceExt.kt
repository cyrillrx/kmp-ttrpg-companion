package com.cyrillrx.rpg.dnd.domain

import kotlin.math.roundToInt

private const val DND_FEET_STEP = 5
private const val DND_METERS_STEP = 1.5f

fun Int.feetToMeters(): Float = this / DND_FEET_STEP.toFloat() * DND_METERS_STEP

fun Float.metersToFeet(): Int = (this / DND_METERS_STEP * DND_FEET_STEP).roundToInt()
