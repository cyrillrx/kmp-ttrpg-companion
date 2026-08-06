package com.cyrillrx.rpg.core.presentation.format

import com.cyrillrx.rpg.dnd.domain.feetToMeters
import com.cyrillrx.rpg.settings.domain.DistanceUnit

private fun Float.toMetersString(): String =
    if (this % 1 == 0f) "${this.toInt()} m" else "$this m"

internal fun Int.toDistanceString(unit: DistanceUnit): String = when (unit) {
    DistanceUnit.FEET -> "$this ft."
    DistanceUnit.METERS -> feetToMeters().toMetersString()
}
