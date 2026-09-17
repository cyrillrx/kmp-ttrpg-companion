package com.cyrillrx.rpg.core.presentation.format

import com.cyrillrx.rpg.dnd.domain.feetToMeters
import com.cyrillrx.rpg.settings.domain.DistanceUnit

private fun Float.toTrimmedString(): String = if (this % 1 == 0f) "${this.toInt()}" else "$this"

internal fun Int.toDistanceString(unit: DistanceUnit): String = when (unit) {
    DistanceUnit.FEET -> "$this ft."
    DistanceUnit.METERS -> "${feetToMeters().toTrimmedString()} m"
}

/** The converted amount alone, for callers that already carry the unit in a label. */
internal fun Int.toDistanceValue(unit: DistanceUnit): String = when (unit) {
    DistanceUnit.FEET -> toString()
    DistanceUnit.METERS -> feetToMeters().toTrimmedString()
}
