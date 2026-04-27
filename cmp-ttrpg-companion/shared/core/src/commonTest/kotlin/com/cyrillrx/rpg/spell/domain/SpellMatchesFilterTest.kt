package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SpellMatchesFilterTest {

    private val fireball = SampleSpellRepository.fireball()
    private val mageArmor = SampleSpellRepository.mageArmor()

    @Test
    fun `default filter matches any spell`() {
        val filter = SpellFilter()
        assertTrue(fireball.matches(filter))
        assertTrue(mageArmor.matches(filter))
    }

    @Test
    fun `filter by matching school matches`() {
        val filter = SpellFilter(schools = setOf(Spell.School.EVOCATION))
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by non-matching school does not match`() {
        val filter = SpellFilter(schools = setOf(Spell.School.ABJURATION))
        assertFalse(fireball.matches(filter))
    }

    @Test
    fun `filter by matching level matches`() {
        val filter = SpellFilter(levels = setOf(3))
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by non-matching level does not match`() {
        val filter = SpellFilter(levels = setOf(1))
        assertFalse(fireball.matches(filter))
    }

    @Test
    fun `filter by matching class matches`() {
        val filter = SpellFilter(playerClasses = setOf(PlayerCharacter.Class.SORCERER))
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by non-matching class does not match`() {
        val filter = SpellFilter(playerClasses = setOf(PlayerCharacter.Class.PALADIN))
        assertFalse(fireball.matches(filter))
    }

    @Test
    fun `filter by text query matches title`() {
        val filter = SpellFilter(query = "fireball")
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by text query matches title with different case`() {
        val filter = SpellFilter(query = "FIREBALL")
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by text query matches description`() {
        val filter = SpellFilter(query = "explosion of flame")
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by french query matches french title`() {
        val filter = SpellFilter(query = "boule de feu")
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `filter by french query matches french description`() {
        val filter = SpellFilter(query = "explosion de flammes")
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `english query still matches when spell has both translations`() {
        val filter = SpellFilter(query = "fireball")
        assertTrue(fireball.matches(filter))
    }

    @Test
    fun `french query does not match english-only spell`() {
        val filter = SpellFilter(query = "armure de mage")
        assertFalse(mageArmor.matches(filter))
    }
}
