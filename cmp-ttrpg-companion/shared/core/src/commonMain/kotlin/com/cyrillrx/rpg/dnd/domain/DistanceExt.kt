package com.cyrillrx.rpg.dnd.domain

const val DND_FEET_STEP = 5
internal const val DND_METERS_STEP = 1.5f

fun Int.feetToMeters(): Float = this / DND_FEET_STEP.toFloat() * DND_METERS_STEP
