package com.cyrillrx.rpg.settings.domain

data class UserPreferences(
    val theme: Theme = Theme.AUTO,
    val distanceUnit: DistanceUnit = DistanceUnit.FEET,
)
