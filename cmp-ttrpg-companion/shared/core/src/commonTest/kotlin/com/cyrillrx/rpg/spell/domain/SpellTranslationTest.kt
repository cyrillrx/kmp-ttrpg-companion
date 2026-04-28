package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SpellTranslationTest {

    @Test
    fun `spell with empty translations throws`() {
        assertFailsWith<IllegalArgumentException> {
            Spell(
                id = "test-spell",
                source = "srd_5.1",
                level = 0,
                school = Spell.School.EVOCATION,
                concentration = false,
                ritual = false,
                components = Spell.Components(verbal = false, somatic = false, material = false),
                availableClasses = emptyList(),
                translations = emptyMap(),
            )
        }
    }

    @Test
    fun `resolveTranslation returns requested locale`() {
        val spell = spellWithTranslations("en", "fr")
        val name = spell.resolveTranslation("fr").name
        assertEquals(expected = "fr-name", actual = name)
    }

    @Test
    fun `resolveTranslation falls back to en when locale is missing`() {
        val spell = spellWithTranslations("en")
        val name = spell.resolveTranslation("fr").name
        assertEquals(expected = "en-name", actual = name)
    }

    @Test
    fun `resolveTranslation falls back to first alphabetically when en is missing`() {
        val spell = spellWithTranslations("de", "fr")
        val name = spell.resolveTranslation("es").name
        assertEquals(expected = "de-name", actual = name)
    }

    private fun spellWithTranslations(vararg locales: String) = Spell(
        id = "test-spell",
        source = "test",
        level = 0,
        school = Spell.School.EVOCATION,
        concentration = false,
        ritual = false,
        components = Spell.Components(verbal = false, somatic = false, material = false),
        availableClasses = listOf(PlayerCharacter.Class.WIZARD),
        translations = locales.associateWith { locale ->
            Spell.Translation(
                name = "$locale-name",
                castingTime = "1 action",
                range = "Self",
                duration = "Instantaneous",
                materialDescription = null,
                description = "$locale-description",
            )
        },
    )
}
