package com.cyrillrx.rpg.core.presentation.format

import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.ClassLevels
import com.cyrillrx.rpg.character.domain.DEFAULT_CLASS_LEVELS
import com.cyrillrx.rpg.creature.domain.Proficiency
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterFormatTest {

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

    @Test
    fun `sortedByLevelThenName orders by decreasing level`() {
        val classes: ClassLevels = mapOf(
            Character.Class.ROGUE to 2,
            Character.Class.WIZARD to 5,
            Character.Class.FIGHTER to 3,
        )

        assertEquals(
            expected = listOf(Character.Class.WIZARD, Character.Class.FIGHTER, Character.Class.ROGUE),
            actual = classes.sortedByLevelThenName(FRENCH_NAMES::getValue).map { it.first },
        )
    }

    @Test
    fun `sortedByLevelThenName breaks ties alphabetically in the current language`() {
        val classes: ClassLevels = mapOf(
            Character.Class.RANGER to 3,
            Character.Class.ROGUE to 3,
        )

        // "Rôdeur" before "Roublard", but "Ranger" before "Rogue".
        assertEquals(
            expected = listOf(Character.Class.RANGER, Character.Class.ROGUE),
            actual = classes.sortedByLevelThenName(FRENCH_NAMES::getValue).map { it.first },
        )
        assertEquals(
            expected = listOf(Character.Class.RANGER, Character.Class.ROGUE),
            actual = classes.sortedByLevelThenName(ENGLISH_NAMES::getValue).map { it.first },
        )
    }

    @Test
    fun `sortedByLevelThenName follows the language when the two orders disagree`() {
        val classes: ClassLevels = mapOf(
            Character.Class.FIGHTER to 3,
            Character.Class.SORCERER to 3,
        )

        // "Fighter" before "Sorcerer" in English, but "Ensorceleur" before "Guerrier" in French.
        assertEquals(
            expected = listOf(Character.Class.FIGHTER, Character.Class.SORCERER),
            actual = classes.sortedByLevelThenName(ENGLISH_NAMES::getValue).map { it.first },
        )
        assertEquals(
            expected = listOf(Character.Class.SORCERER, Character.Class.FIGHTER),
            actual = classes.sortedByLevelThenName(FRENCH_NAMES::getValue).map { it.first },
        )
    }

    @Test
    fun `toClassNames joins localized class names by decreasing level`() {
        val classes: ClassLevels = mapOf(
            Character.Class.ROGUE to 2,
            Character.Class.WIZARD to 5,
            Character.Class.FIGHTER to 3,
        )

        assertEquals(expected = "Magicien/Guerrier/Roublard", actual = classes.toClassNames(FRENCH_NAMES::getValue))
        assertEquals(expected = "Wizard/Fighter/Rogue", actual = classes.toClassNames(ENGLISH_NAMES::getValue))
    }

    @Test
    fun `toClassNames breaks ties on the localized name`() {
        val classes: ClassLevels = mapOf(
            Character.Class.FIGHTER to 3,
            Character.Class.SORCERER to 3,
        )

        assertEquals(expected = "Ensorceleur/Guerrier", actual = classes.toClassNames(FRENCH_NAMES::getValue))
        assertEquals(expected = "Fighter/Sorcerer", actual = classes.toClassNames(ENGLISH_NAMES::getValue))
    }

    @Test
    fun `toClassNames renders a single class without a separator`() {
        val classes: ClassLevels = mapOf(Character.Class.ROGUE to 4)

        assertEquals(expected = "Roublard", actual = classes.toClassNames(FRENCH_NAMES::getValue))
    }

    @Test
    fun `toClassNames drops the unspecified class`() {
        val classes: ClassLevels = mapOf(
            Character.Class.FIGHTER to 3,
            Character.Class.UNKNOWN to 1,
        )

        assertEquals(expected = "Guerrier", actual = classes.toClassNames(FRENCH_NAMES::getValue))
        assertEquals(expected = "", actual = DEFAULT_CLASS_LEVELS.toClassNames(FRENCH_NAMES::getValue))
    }

    @Test
    fun `toClassNames is empty for an empty map`() {
        assertEquals(
            expected = "",
            actual = emptyMap<Character.Class, Int>().toClassNames(FRENCH_NAMES::getValue),
        )
    }

    @Test
    fun `toClassBreakdown pairs each class with its level by decreasing level`() {
        val classes: ClassLevels = mapOf(
            Character.Class.ROGUE to 2,
            Character.Class.WIZARD to 5,
        )

        assertEquals(expected = "Magicien 5 / Roublard 2", actual = classes.toClassBreakdown(FRENCH_NAMES::getValue))
        assertEquals(expected = "Wizard 5 / Rogue 2", actual = classes.toClassBreakdown(ENGLISH_NAMES::getValue))
    }

    @Test
    fun `toClassBreakdown omits the level of a lone class`() {
        val classes: ClassLevels = mapOf(Character.Class.FIGHTER to 7)

        assertEquals(expected = "Guerrier", actual = classes.toClassBreakdown(FRENCH_NAMES::getValue))
        assertEquals(expected = "Inconnue", actual = DEFAULT_CLASS_LEVELS.toClassBreakdown(FRENCH_NAMES::getValue))
    }

    @Test
    fun `toClassBreakdown is empty for an empty map`() {
        assertEquals(
            expected = "",
            actual = emptyMap<Character.Class, Int>().toClassBreakdown(FRENCH_NAMES::getValue),
        )
    }

    private companion object {
        val FRENCH_NAMES = mapOf(
            Character.Class.FIGHTER to "Guerrier",
            Character.Class.RANGER to "Rôdeur",
            Character.Class.ROGUE to "Roublard",
            Character.Class.SORCERER to "Ensorceleur",
            Character.Class.WIZARD to "Magicien",
            Character.Class.UNKNOWN to "Inconnue",
        )

        val ENGLISH_NAMES = mapOf(
            Character.Class.FIGHTER to "Fighter",
            Character.Class.RANGER to "Ranger",
            Character.Class.ROGUE to "Rogue",
            Character.Class.SORCERER to "Sorcerer",
            Character.Class.WIZARD to "Wizard",
            Character.Class.UNKNOWN to "Unknown",
        )
    }
}
