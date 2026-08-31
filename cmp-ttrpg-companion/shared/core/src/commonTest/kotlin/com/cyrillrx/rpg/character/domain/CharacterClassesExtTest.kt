package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterClassesExtTest {

    private val fighter = SampleCharacterRepository.humanFighter()
    private val multiclass = SampleCharacterRepository.multiclassBarbarian()

    @Test
    fun `adding a class to a classless character makes it the primary one`() {
        val classless = fighter.copy(classes = DEFAULT_CLASS_LEVELS, primaryClass = Character.Class.UNKNOWN)

        val updated = classless.withClassAdded(Character.Class.BARD)

        assertEquals(mapOf(Character.Class.BARD to 1), updated.classes)
        assertEquals(Character.Class.BARD, updated.primaryClass)
    }

    @Test
    fun `adding a class keeps the current primary class`() {
        val updated = fighter.withClassAdded(Character.Class.ROGUE)

        assertEquals(mapOf(Character.Class.FIGHTER to 1, Character.Class.ROGUE to 1), updated.classes)
        assertEquals(Character.Class.FIGHTER, updated.primaryClass)
    }

    @Test
    fun `adding an already assigned class changes nothing`() {
        assertEquals(multiclass, multiclass.withClassAdded(Character.Class.SORCERER))
    }

    @Test
    fun `adding a class is refused once the total level is reached`() {
        val capped = fighter.withClassLevel(Character.Class.FIGHTER, MAX_CHARACTER_LEVEL)

        assertEquals(capped, capped.withClassAdded(Character.Class.ROGUE))
    }

    @Test
    fun `removing the primary class promotes the highest remaining level`() {
        val updated = multiclass.withClassRemoved(Character.Class.BARBARIAN)

        assertEquals(mapOf(Character.Class.SORCERER to 4, Character.Class.WARLOCK to 3), updated.classes)
        assertEquals(Character.Class.SORCERER, updated.primaryClass)
    }

    @Test
    fun `removing a secondary class keeps the primary one`() {
        val updated = multiclass.withClassRemoved(Character.Class.SORCERER)

        assertEquals(mapOf(Character.Class.BARBARIAN to 5, Character.Class.WARLOCK to 3), updated.classes)
        assertEquals(Character.Class.BARBARIAN, updated.primaryClass)
    }

    @Test
    fun `removing the last class falls back to an unspecified class at level 1`() {
        val updated = fighter.withClassRemoved(Character.Class.FIGHTER)

        assertEquals(DEFAULT_CLASS_LEVELS, updated.classes)
        assertEquals(Character.Class.UNKNOWN, updated.primaryClass)
    }

    @Test
    fun `removing a class that is not assigned changes nothing`() {
        assertEquals(fighter, fighter.withClassRemoved(Character.Class.DRUID))
    }

    @Test
    fun `a level is clamped so that the total stays within the cap`() {
        val updated = multiclass.withClassLevel(Character.Class.SORCERER, MAX_CHARACTER_LEVEL)

        assertEquals(12, updated.classes[Character.Class.SORCERER])
        assertEquals(MAX_CHARACTER_LEVEL, updated.totalLevel)
    }

    @Test
    fun `a level below the minimum is clamped`() {
        val updated = fighter.withClassLevel(Character.Class.FIGHTER, 0)

        assertEquals(MIN_CHARACTER_LEVEL, updated.classes[Character.Class.FIGHTER])
    }

    @Test
    fun `setting the level of an unassigned class changes nothing`() {
        assertEquals(fighter, fighter.withClassLevel(Character.Class.MONK, 5))
    }

    @Test
    fun `maxLevelFor leaves the other class levels untouched`() {
        assertEquals(12, multiclass.maxLevelFor(Character.Class.SORCERER))
        assertEquals(11, multiclass.maxLevelFor(Character.Class.WARLOCK))
        assertEquals(MAX_CHARACTER_LEVEL, fighter.maxLevelFor(Character.Class.FIGHTER))
    }

    @Test
    fun `raising a class above the primary makes it the new primary`() {
        val updated = multiclass.withClassLevel(Character.Class.WARLOCK, 6)

        assertEquals(Character.Class.WARLOCK, updated.primaryClass)
    }

    @Test
    fun `lowering the primary below another class reassigns the primary`() {
        val updated = multiclass.withClassLevel(Character.Class.BARBARIAN, 1)

        assertEquals(Character.Class.SORCERER, updated.primaryClass)
    }

    @Test
    fun `a class tied with the primary keeps the current primary`() {
        val updated = multiclass.withClassLevel(Character.Class.SORCERER, 5)

        assertEquals(Character.Class.BARBARIAN, updated.primaryClass)
    }

    @Test
    fun `lowering a secondary class keeps the primary one`() {
        val updated = multiclass.withClassLevel(Character.Class.WARLOCK, 1)

        assertEquals(Character.Class.BARBARIAN, updated.primaryClass)
    }
}
