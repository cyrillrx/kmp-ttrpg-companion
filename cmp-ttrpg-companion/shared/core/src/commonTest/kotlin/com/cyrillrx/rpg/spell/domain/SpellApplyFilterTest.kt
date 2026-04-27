package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpellApplyFilterTest {

    private val allSpells = SampleSpellRepository.getAll()

    @Test
    fun `null filter returns the full list`() {
        val result = allSpells.applyFilter(null)
        assertEquals(allSpells, result)
    }

    @Test
    fun `default filter returns all spells`() {
        val result = allSpells.applyFilter(SpellFilter())
        assertEquals(allSpells.size, result.size)
    }

    @Test
    fun `filter by school keeps only matching spells`() {
        val result = allSpells.applyFilter(SpellFilter(schools = setOf(Spell.School.EVOCATION)))
        assertTrue(result.all { it.school == Spell.School.EVOCATION })
        assertEquals(2, result.size)
    }

    @Test
    fun `filter by level keeps only matching spells`() {
        val result = allSpells.applyFilter(SpellFilter(levels = setOf(3)))
        assertTrue(result.all { it.level == 3 })
        assertEquals(2, result.size)
    }

    @Test
    fun `filter by class keeps only matching spells`() {
        val result = allSpells.applyFilter(SpellFilter(playerClasses = setOf(PlayerCharacter.Class.BARD)))
        assertTrue(result.all { it.availableClasses.contains(PlayerCharacter.Class.BARD) })
        assertEquals(1, result.size)
    }

    @Test
    fun `filter by query keeps only matching spells`() {
        val result = allSpells.applyFilter(SpellFilter(query = "fireball"))
        assertEquals(1, result.size)
        assertEquals("Fireball", result.first().title)
    }

    @Test
    fun `filter with no match returns empty list`() {
        val result = allSpells.applyFilter(SpellFilter(playerClasses = setOf(PlayerCharacter.Class.PALADIN)))
        assertTrue(result.isEmpty())
    }
}
