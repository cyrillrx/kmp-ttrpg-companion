package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterDisplayNameTest {

    @Test
    fun `displayName returns the root name whatever the locale`() {
        val character = SampleCharacterRepository.humanFighter().copy(name = "Elandra")

        assertEquals(expected = "Elandra", actual = character.displayName("en"))
        assertEquals(expected = "Elandra", actual = character.displayName("fr"))
    }
}
