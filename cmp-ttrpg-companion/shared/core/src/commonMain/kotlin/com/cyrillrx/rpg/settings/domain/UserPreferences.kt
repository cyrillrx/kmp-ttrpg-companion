package com.cyrillrx.rpg.settings.domain

data class UserPreferences(
    val theme: Theme = Theme.SYSTEM,
    val palette: Palette = Palette.ARCANE,
    val distanceUnit: DistanceUnit = DistanceUnit.FEET,
)
