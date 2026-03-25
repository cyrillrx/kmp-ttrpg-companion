package com.cyrillrx.rpg.spell.data

import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.domain.Spell

class SampleSpellRepository {
    fun getAll(): List<Spell> = listOf(
        fireball(),
        mageArmor(),
        detectThoughts(),
        thunderwave(),
        counterspell(),
    )

    fun get(): Spell = fireball()

    fun getById(id: String): Spell? = getAll().firstOrNull { it.id == id }

    companion object {
        private fun fireball() = Spell(
            id = "Fireball",
            title = "Fireball",
            description = "A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame.",
            level = 3,
            castingTime = "1 action",
            range = "45 meters",
            components = "V, S, M",
            duration = "Instantaneous",
            schools = listOf(Spell.School.EVOCATION),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
        )

        private fun mageArmor() = Spell(
            id = "Mage Armor",
            title = "Mage Armor",
            description = "You touch a willing creature who isn't wearing armor, and a protective magical force surrounds it until the spell ends.",
            level = 1,
            castingTime = "1 action",
            range = "Touch",
            components = "V, S, M",
            duration = "8 hours",
            schools = listOf(Spell.School.ABJURATION),
            availableClasses = listOf(PlayerCharacter.Class.WIZARD),
        )

        private fun detectThoughts() = Spell(
            id = "Detect Thoughts",
            title = "Detect Thoughts",
            description = "For the duration, you can read the thoughts of certain creatures.",
            level = 2,
            castingTime = "1 action",
            range = "Self",
            components = "V, S, M",
            duration = "Concentration, up to 1 minute",
            schools = listOf(Spell.School.DIVINATION),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
            ),
        )

        private fun thunderwave() = Spell(
            id = "Thunderwave",
            title = "Thunderwave",
            description = "A wave of thunderous force sweeps out from you.",
            level = 1,
            castingTime = "1 action",
            range = "Self (15-foot cube)",
            components = "V, S",
            duration = "Instantaneous",
            schools = listOf(Spell.School.EVOCATION),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
                PlayerCharacter.Class.BARD,
            ),
        )

        private fun counterspell() = Spell(
            id = "Counterspell",
            title = "Counterspell",
            description = "You attempt to interrupt a creature in the process of casting a spell.",
            level = 3,
            castingTime = "1 reaction when you see an opponent in a 10-foot radius",
            range = "60 feet",
            components = "S",
            duration = "Instantaneous",
            schools = listOf(Spell.School.ABJURATION),
            availableClasses = listOf(
                PlayerCharacter.Class.SORCERER,
                PlayerCharacter.Class.WIZARD,
                PlayerCharacter.Class.WARLOCK,
            ),
        )
    }
}
