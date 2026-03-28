package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpellFilterTest {

    @Test
    fun `hasActiveFilters is false for default filter`() {
        val defaultFilter = SpellFilter()
        assertFalse(defaultFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when schools are set`() {
        val schoolsFilter = SpellFilter(schools = setOf(Spell.School.EVOCATION))
        assertTrue(schoolsFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when levels are set`() {
        val levelsFilter = SpellFilter(levels = setOf(3))
        assertTrue(levelsFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is true when classes are set`() {
        val classesFilter = SpellFilter(playerClasses = setOf(PlayerCharacter.Class.WIZARD))
        assertTrue(classesFilter.hasActiveFilters)
    }

    @Test
    fun `hasActiveFilters is false when only query is set`() {
        val queryFilter = SpellFilter(query = "fireball")
        assertFalse(queryFilter.hasActiveFilters)
    }
}
