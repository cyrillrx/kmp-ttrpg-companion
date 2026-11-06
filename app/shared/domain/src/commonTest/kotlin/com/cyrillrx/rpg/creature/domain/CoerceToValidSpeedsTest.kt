package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.character.domain.coerceToValidCharacterSpeeds
import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidSpeedsTest {

    @Test
    fun `creature speeds coerce every movement mode`() {
        val coerced = Speeds(walk = 3, fly = 9999, swim = -10, climb = 33, burrow = 60)
            .coerceToValidCreatureSpeeds()

        assertEquals(Speeds(walk = 5, fly = 200, swim = 0, climb = 35, burrow = 60), coerced)
    }

    @Test
    fun `creature speeds keep the hover flag`() {
        val coerced = Speeds(walk = 30, fly = 150, hover = true).coerceToValidCreatureSpeeds()

        assertEquals(true, coerced.hover)
    }

    @Test
    fun `character walk speed is held to the narrower range`() {
        val tooSlow = Speeds(walk = 5).coerceToValidCharacterSpeeds()
        assertEquals(25, tooSlow.walk)

        val tooFast = Speeds(walk = 200).coerceToValidCharacterSpeeds()
        assertEquals(120, tooFast.walk)
    }

    @Test
    fun `character keeps the other movement modes within the creature range`() {
        val coerced = Speeds(walk = 30, fly = 9999, swim = 33).coerceToValidCharacterSpeeds()

        assertEquals(Speeds(walk = 30, fly = 200, swim = 35), coerced)
    }
}
