package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Dangerous
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

class SpellFormatTest {

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

    @Test
    fun `School getIcon maps every school and the null case to an icon`() {
        assertEquals(expected = Icons.Filled.Shield, actual = Spell.School.ABJURATION.getIcon())
        assertEquals(expected = Icons.Filled.Flare, actual = Spell.School.CONJURATION.getIcon())
        assertEquals(expected = Icons.Filled.Visibility, actual = Spell.School.DIVINATION.getIcon())
        assertEquals(expected = Icons.Filled.Psychology, actual = Spell.School.ENCHANTMENT.getIcon())
        assertEquals(expected = Icons.Filled.Bolt, actual = Spell.School.EVOCATION.getIcon())
        assertEquals(expected = Icons.Filled.AutoAwesome, actual = Spell.School.ILLUSION.getIcon())
        assertEquals(expected = Icons.Outlined.Dangerous, actual = Spell.School.NECROMANCY.getIcon())
        assertEquals(expected = Icons.Filled.SwapHoriz, actual = Spell.School.TRANSMUTATION.getIcon())
        assertEquals(expected = Icons.Filled.AutoAwesome, actual = (null as Spell.School?).getIcon())
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
