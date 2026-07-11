package com.cyrillrx.rpg.core.presentation.component.dnd

import com.cyrillrx.rpg.core.presentation.theme.SchoolAbjuration
import com.cyrillrx.rpg.core.presentation.theme.SchoolConjuration
import com.cyrillrx.rpg.core.presentation.theme.SchoolDivination
import com.cyrillrx.rpg.core.presentation.theme.SchoolEnchantment
import com.cyrillrx.rpg.core.presentation.theme.SchoolEvocation
import com.cyrillrx.rpg.core.presentation.theme.SchoolIllusion
import com.cyrillrx.rpg.core.presentation.theme.SchoolNecromancy
import com.cyrillrx.rpg.core.presentation.theme.SchoolTransmutation
import com.cyrillrx.rpg.spell.domain.Spell
import kotlin.test.Test
import kotlin.test.assertEquals

class SpellFormatExtTest {

    @Test
    fun `getFormattedComponents joins verbal, somatic and material`() {
        val spell = spellWith(verbal = true, somatic = true, material = true)
        assertEquals(expected = "V, S, M", actual = spell.getFormattedComponents())
    }

    @Test
    fun `getFormattedComponents omits material when material is absent`() {
        val spell = spellWith(verbal = true, somatic = true, material = false)
        assertEquals(expected = "V, S", actual = spell.getFormattedComponents())
    }

    @Test
    fun `getFormattedComponents returns each single component alone`() {
        val verbalOnly = spellWith(verbal = true, somatic = false, material = false)
        val somaticOnly = spellWith(verbal = false, somatic = true, material = false)
        val materialOnly = spellWith(verbal = false, somatic = false, material = true)
        assertEquals(expected = "V", actual = verbalOnly.getFormattedComponents())
        assertEquals(expected = "S", actual = somaticOnly.getFormattedComponents())
        assertEquals(expected = "M", actual = materialOnly.getFormattedComponents())
    }

    @Test
    fun `getFormattedComponents returns empty string when there are no components`() {
        val spell = spellWith(verbal = false, somatic = false, material = false)
        assertEquals(expected = "", actual = spell.getFormattedComponents())
    }

    @Test
    fun `School getColor maps every school to its accent color`() {
        assertEquals(expected = SchoolAbjuration, actual = Spell.School.ABJURATION.getColor())
        assertEquals(expected = SchoolConjuration, actual = Spell.School.CONJURATION.getColor())
        assertEquals(expected = SchoolDivination, actual = Spell.School.DIVINATION.getColor())
        assertEquals(expected = SchoolEnchantment, actual = Spell.School.ENCHANTMENT.getColor())
        assertEquals(expected = SchoolEvocation, actual = Spell.School.EVOCATION.getColor())
        assertEquals(expected = SchoolIllusion, actual = Spell.School.ILLUSION.getColor())
        assertEquals(expected = SchoolNecromancy, actual = Spell.School.NECROMANCY.getColor())
        assertEquals(expected = SchoolTransmutation, actual = Spell.School.TRANSMUTATION.getColor())
    }

    @Test
    fun `Spell getColor delegates to its school`() {
        val spell = spellWith(school = Spell.School.NECROMANCY)
        assertEquals(expected = SchoolNecromancy, actual = spell.getColor())
    }

    private fun spellWith(
        verbal: Boolean = true,
        somatic: Boolean = true,
        material: Boolean = true,
        school: Spell.School = Spell.School.EVOCATION,
    ) = Spell(
        id = "test-spell",
        source = "test",
        level = 1,
        school = school,
        concentration = false,
        ritual = false,
        components = Spell.Components(verbal = verbal, somatic = somatic, material = material),
        availableClasses = emptyList(),
        translations = mapOf(
            "en" to Spell.Translation(
                name = "Test Spell",
                castingTime = "1 action",
                range = "Self",
                duration = "Instantaneous",
                materialDescription = null,
                description = "A test spell.",
            ),
        ),
    )
}
