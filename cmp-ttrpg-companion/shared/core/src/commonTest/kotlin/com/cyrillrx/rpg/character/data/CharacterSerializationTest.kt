package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.Character
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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

    @Test
    fun `lastModified defaults to epoch zero when absent from the json`() {
        // Default values are not encoded, so a freshly-built character (epoch 0) is
        // serialized without the key — exactly like characters stored before the field existed.
        val json = SampleCharacterRepository.humanFighter().serialize()
        assertFalse(json.contains("lastModified"), "expected lastModified to be omitted, was: $json")

        val restored = json.deserialize<Character>()
        assertEquals(Instant.fromEpochMilliseconds(0L), restored.lastModified)
    }

    @Test
    fun `lastModified survives a serialization round-trip`() {
        val original = SampleCharacterRepository.humanFighter()
            .copy(lastModified = Instant.fromEpochMilliseconds(1_700_000_000_000L))

        val restored = original.serialize().deserialize<Character>()

        assertEquals(original.lastModified, restored.lastModified)
    }
}
