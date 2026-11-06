package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.Character
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterSerializationTest {

    @Test
    fun `serializing and deserializing a character preserves all fields`() {
        val original = SampleCharacterRepository.humanFighter()
        val roundTripped = original.serialize().deserialize<Character>()
        assertEquals(original, roundTripped)
    }

    @Test
    fun `currentHitPoints and temporaryHitPoints are preserved after serialization`() {
        val original = SampleCharacterRepository.humanFighter()
            .copy(currentHitPoints = 5, temporaryHitPoints = 3)
        val roundTripped = original.serialize().deserialize<Character>()
        assertEquals(5, roundTripped.currentHitPoints)
        assertEquals(3, roundTripped.temporaryHitPoints)
    }
}
