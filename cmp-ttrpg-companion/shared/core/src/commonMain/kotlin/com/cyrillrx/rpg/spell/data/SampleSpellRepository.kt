package com.cyrillrx.rpg.spell.data

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellComponents
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.domain.applyFilter

class SampleSpellRepository : SpellRepository {
    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        return spells.applyFilter(filter)
    }

    override suspend fun getById(id: String): Spell? = spells.firstOrNull { it.id == id }

    companion object {
        private val spells: List<Spell> = listOf(
            fireball(),
            mageArmor(),
            detectThoughts(),
            thunderwave(),
            counterspell(),
        )

        fun getAll(): List<Spell> = spells

        fun getFirst(): Spell = spells.first()

        fun fireball() = Spell(
            id = "fireball",
            source = "srd_5.1",
            level = 3,
            school = Spell.School.EVOCATION,
            concentration = false,
            ritual = false,
            components = SpellComponents(verbal = true, somatic = true, material = true),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
            translations = mapOf(
                "en" to Spell.Translation(
                    name = "Fireball",
                    castingTime = "1 action",
                    range = "150 feet",
                    duration = "Instantaneous",
                    materialDescription = "a tiny ball of bat guano and sulfur",
                    description = "A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame.",
                ),
                "fr" to Spell.Translation(
                    name = "Boule de feu",
                    castingTime = "1 action",
                    range = "45 mètres",
                    duration = "Instantanée",
                    materialDescription = "une minuscule bille de fiente de chauve-souris et de soufre",
                    description = "Un trait lumineux jaillit de votre doigt pointé vers un point choisi à portée, puis explose dans un grondement sourd en une explosion de flammes.",
                ),
            ),
        )

        fun mageArmor() = Spell(
            id = "mage-armor",
            source = "srd_5.1",
            level = 1,
            school = Spell.School.ABJURATION,
            concentration = false,
            ritual = false,
            components = SpellComponents(verbal = true, somatic = true, material = true),
            availableClasses = listOf(PlayerCharacter.Class.WIZARD),
            translations = mapOf(
                "en" to Spell.Translation(
                    name = "Mage Armor",
                    castingTime = "1 action",
                    range = "Touch",
                    duration = "8 hours",
                    materialDescription = "a piece of cured leather",
                    description = "You touch a willing creature who isn't wearing armor, and a protective magical force surrounds it until the spell ends.",
                ),
            ),
        )

        private fun detectThoughts() = Spell(
            id = "detect-thoughts",
            source = "srd_5.1",
            level = 2,
            school = Spell.School.DIVINATION,
            concentration = true,
            ritual = false,
            components = SpellComponents(verbal = true, somatic = true, material = true),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
            translations = mapOf(
                "en" to Spell.Translation(
                    name = "Detect Thoughts",
                    castingTime = "1 action",
                    range = "Self",
                    duration = "Concentration, up to 1 minute",
                    materialDescription = "a copper piece",
                    description = "For the duration, you can read the thoughts of certain creatures.",
                ),
            ),
        )

        private fun thunderwave() = Spell(
            id = "thunderwave",
            source = "srd_5.1",
            level = 1,
            school = Spell.School.EVOCATION,
            concentration = false,
            ritual = false,
            components = SpellComponents(verbal = true, somatic = true, material = false),
            availableClasses = listOf(
                PlayerCharacter.Class.BARD,
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
            translations = mapOf(
                "en" to Spell.Translation(
                    name = "Thunderwave",
                    castingTime = "1 action",
                    range = "Self (15-foot cube)",
                    duration = "Instantaneous",
                    materialDescription = null,
                    description = "A wave of thunderous force sweeps out from you.",
                ),
            ),
        )

        private fun counterspell() = Spell(
            id = "counterspell",
            source = "srd_5.1",
            level = 3,
            school = Spell.School.ABJURATION,
            concentration = false,
            ritual = false,
            components = SpellComponents(verbal = false, somatic = true, material = false),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WARLOCK,
                PlayerCharacter.Class.WIZARD,
            ),
            translations = mapOf(
                "en" to Spell.Translation(
                    name = "Counterspell",
                    castingTime = "1 reaction",
                    range = "60 feet",
                    duration = "Instantaneous",
                    materialDescription = null,
                    description = "You attempt to interrupt a creature in the process of casting a spell.",
                ),
            ),
        )
    }
}
