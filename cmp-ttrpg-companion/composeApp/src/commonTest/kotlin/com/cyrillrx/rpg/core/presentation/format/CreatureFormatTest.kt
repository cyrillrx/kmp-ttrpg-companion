package com.cyrillrx.rpg.core.presentation.format

import com.cyrillrx.rpg.settings.domain.DistanceUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class CreatureFormatTest {

    @Test
    fun `toDistanceString in feet appends the ft suffix`() {
        assertEquals(expected = "30 ft.", actual = 30.toDistanceString(DistanceUnit.FEET))
    }

    @Test
    fun `toDistanceString in meters drops the decimal for whole values`() {
        // 10 ft converts to 3.0 m
        assertEquals(expected = "3 m", actual = 10.toDistanceString(DistanceUnit.METERS))
    }

    @Test
    fun `toDistanceString in meters keeps the decimal for fractional values`() {
        // 5 ft converts to 1.5 m
        assertEquals(expected = "1.5 m", actual = 5.toDistanceString(DistanceUnit.METERS))
    }
}
