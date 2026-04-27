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
            title = "Fireball",
            description = "A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame.",
            level = 3,
            school = Spell.School.EVOCATION,
            concentration = false,
            ritual = false,
            castingTime = "1 action",
            range = "45 meters",
            duration = "Instantaneous",
            components = SpellComponents(verbal = true, somatic = true, material = true),
            materialDescription = "a tiny ball of bat guano and sulfur",
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
        )

        fun mageArmor() = Spell(
            id = "mage-armor",
            source = "srd_5.1",
            title = "Mage Armor",
            description = "You touch a willing creature who isn't wearing armor, and a protective magical force surrounds it until the spell ends.",
            level = 1,
            school = Spell.School.ABJURATION,
            concentration = false,
            ritual = false,
            castingTime = "1 action",
            range = "Touch",
            duration = "8 hours",
            components = SpellComponents(verbal = true, somatic = true, material = true),
            materialDescription = "a piece of cured leather",
            availableClasses = listOf(PlayerCharacter.Class.WIZARD),
        )

        private fun detectThoughts() = Spell(
            id = "detect-thoughts",
            source = "srd_5.1",
            title = "Detect Thoughts",
            description = "For the duration, you can read the thoughts of certain creatures.",
            level = 2,
            school = Spell.School.DIVINATION,
            concentration = true,
            ritual = false,
            castingTime = "1 action",
            range = "Self",
            duration = "Concentration, up to 1 minute",
            components = SpellComponents(verbal = true, somatic = true, material = true),
            materialDescription = "a copper piece",
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
        )

        private fun thunderwave() = Spell(
            id = "thunderwave",
            source = "srd_5.1",
            title = "Thunderwave",
            description = "A wave of thunderous force sweeps out from you.",
            level = 1,
            school = Spell.School.EVOCATION,
            concentration = false,
            ritual = false,
            castingTime = "1 action",
            range = "Self (15-foot cube)",
            duration = "Instantaneous",
            components = SpellComponents(verbal = true, somatic = true, material = false),
            materialDescription = null,
            availableClasses = listOf(
                PlayerCharacter.Class.BARD,
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
        )

        private fun counterspell() = Spell(
            id = "counterspell",
            source = "srd_5.1",
            title = "Counterspell",
            description = "You attempt to interrupt a creature in the process of casting a spell.",
            level = 3,
            school = Spell.School.ABJURATION,
            concentration = false,
            ritual = false,
            castingTime = "1 reaction when you see an opponent in a 10-foot radius",
            range = "60 feet",
            duration = "Instantaneous",
            components = SpellComponents(verbal = false, somatic = true, material = false),
            materialDescription = null,
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WARLOCK,
                PlayerCharacter.Class.WIZARD,
            ),
        )
    }
}
