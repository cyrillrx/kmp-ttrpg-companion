package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.creature.domain.Proficiency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterFormatExtTest {

    @Test
    fun `toSvgPath maps every known class to its drawable`() {
        assertEquals(expected = "drawable/class_artificer.svg", actual = Character.Class.ARTIFICER.toSvgPath())
        assertEquals(expected = "drawable/class_barbarian.svg", actual = Character.Class.BARBARIAN.toSvgPath())
        assertEquals(expected = "drawable/class_bard.svg", actual = Character.Class.BARD.toSvgPath())
        assertEquals(expected = "drawable/class_cleric.svg", actual = Character.Class.CLERIC.toSvgPath())
        assertEquals(expected = "drawable/class_druid.svg", actual = Character.Class.DRUID.toSvgPath())
        assertEquals(expected = "drawable/class_fighter.svg", actual = Character.Class.FIGHTER.toSvgPath())
        assertEquals(expected = "drawable/class_monk.svg", actual = Character.Class.MONK.toSvgPath())
        assertEquals(expected = "drawable/class_paladin.svg", actual = Character.Class.PALADIN.toSvgPath())
        assertEquals(expected = "drawable/class_ranger.svg", actual = Character.Class.RANGER.toSvgPath())
        assertEquals(expected = "drawable/class_rogue.svg", actual = Character.Class.ROGUE.toSvgPath())
        assertEquals(expected = "drawable/class_sorcerer.svg", actual = Character.Class.SORCERER.toSvgPath())
        assertEquals(expected = "drawable/class_warlock.svg", actual = Character.Class.WARLOCK.toSvgPath())
        assertEquals(expected = "drawable/class_wizard.svg", actual = Character.Class.WIZARD.toSvgPath())
    }

    @Test
    fun `toSvgPath assigns a distinct drawable to each known class`() {
        val knownClasses = Character.Class.entries.filter { it != Character.Class.UNKNOWN }
        val paths = knownClasses.map { it.toSvgPath() }
        assertEquals(expected = knownClasses.size, actual = paths.toSet().size)
    }

    @Test
    fun `toSvgPath throws for UNKNOWN which has no icon`() {
        assertFailsWith<IllegalStateException> { Character.Class.UNKNOWN.toSvgPath() }
    }

    @Test
    fun `getFontWeight is bold for proficient and expert, normal otherwise`() {
        assertEquals(expected = FontWeight.Normal, actual = Proficiency.NONE.getFontWeight())
        assertEquals(expected = FontWeight.Bold, actual = Proficiency.PROFICIENT.getFontWeight())
        assertEquals(expected = FontWeight.Bold, actual = Proficiency.EXPERT.getFontWeight())
    }
}
