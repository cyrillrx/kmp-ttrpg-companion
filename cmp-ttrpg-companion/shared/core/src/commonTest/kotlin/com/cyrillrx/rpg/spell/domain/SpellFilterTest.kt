package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpellFilterTest {

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
