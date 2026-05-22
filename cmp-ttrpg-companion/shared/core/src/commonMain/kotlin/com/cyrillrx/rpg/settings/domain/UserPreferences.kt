package com.cyrillrx.rpg.settings.domain

data class UserPreferences(
    val theme: Theme = Theme.SYSTEM,
    val distanceUnit: DistanceUnit = DistanceUnit.FEET,
)
