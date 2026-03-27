package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpellFilterTest {

    private val fireball = SampleSpellRepository.fireball()
    private val mageArmor = SampleSpellRepository.mageArmor()

    @Test
    fun `default filter matches any spell`() {
        val filter = SpellFilter()
        assertTrue(filter.matches(fireball))
        assertTrue(filter.matches(mageArmor))
    }

    @Test
    fun `filter by matching school matches`() {
        val filter = SpellFilter(schools = setOf(Spell.School.EVOCATION))
        assertTrue(filter.matches(fireball))
    }

    @Test
    fun `filter by non-matching school does not match`() {
        val filter = SpellFilter(schools = setOf(Spell.School.ABJURATION))
        assertFalse(filter.matches(fireball))
    }

    @Test
    fun `filter by matching level matches`() {
        val filter = SpellFilter(levels = setOf(3))
        assertTrue(filter.matches(fireball))
    }

    @Test
    fun `filter by non-matching level does not match`() {
        val filter = SpellFilter(levels = setOf(1))
        assertFalse(filter.matches(fireball))
    }

    @Test
    fun `filter by matching class matches`() {
        val filter = SpellFilter(playerClasses = setOf(PlayerCharacter.Class.SORCERER))
        assertTrue(filter.matches(fireball))
    }

    @Test
    fun `filter by non-matching class does not match`() {
        val filter = SpellFilter(playerClasses = setOf(PlayerCharacter.Class.PALADIN))
        assertFalse(filter.matches(fireball))
    }

    @Test
    fun `filter by text query matches title`() {
        val filter = SpellFilter(query = "fireball")
        assertTrue(filter.matches(fireball))
    }

    @Test
    fun `filter by text query matches title with different case`() {
        val filter = SpellFilter(query = "FIREBALL")
        assertTrue(filter.matches(fireball))
    }

    @Test
    fun `filter by text query matches description`() {
        val filter = SpellFilter(query = "explosion of flame")
        assertTrue(filter.matches(fireball))
    }

    @Test
    fun `hasActiveFilters is false for default filter`() {
        val defaultSpellFilter = SpellFilter()
        assertFalse(defaultSpellFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when schools are set`() {
        val evocationSchoolFilter = SpellFilter(schools = setOf(Spell.School.EVOCATION))
        assertTrue(evocationSchoolFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when levels are set`() {
        val lvl3SpellFilter = SpellFilter(levels = setOf(3))
        assertTrue(lvl3SpellFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when classes are set`() {
        val wizardSpellFilter = SpellFilter(playerClasses = setOf(PlayerCharacter.Class.WIZARD))
        assertTrue(wizardSpellFilter.hasActiveFilters)
    }
}
