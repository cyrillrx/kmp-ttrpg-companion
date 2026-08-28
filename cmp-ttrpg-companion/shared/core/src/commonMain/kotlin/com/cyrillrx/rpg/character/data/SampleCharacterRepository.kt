package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.core.domain.values
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlin.time.Instant

class SampleCharacterRepository : CharacterRepository {
    override suspend fun getAll(filter: CharacterFilter?): List<Stored<Character>> = characters.applyFilter(filter)

    override suspend fun get(id: String): Character? = characters.firstOrNull { it.value.id == id }?.value

    override suspend fun getByIds(ids: List<String>): List<Character> {
        val all = characters.associateBy { it.value.id }
        return ids.mapNotNull { all[it]?.value }
    }

    override suspend fun save(character: Character) = Unit

    override suspend fun delete(id: String) = Unit

    companion object {
        private val characters: List<Stored<Character>> =
            listOf(
                stored(humanFighter(), updatedAt = "2024-01-18T09:15:00Z"),
                stored(elfRogue(), updatedAt = "2024-01-12T17:45:00Z"),
            )

        private fun stored(character: Character, updatedAt: String): Stored<Character> =
            Stored(value = character, updatedAt = Instant.parse(updatedAt))

        fun getAll(): List<Stored<Character>> = characters

        fun getAllValues(): List<Character> = characters.values()

        fun humanFighter() =
            Character(
                id = "sample-fighter",
                name = "Borin Pierrenoire",
                size = Creature.Size.MEDIUM,
                alignment = Creature.Alignment.LAWFUL_GOOD,
                abilities =
                    Abilities(
                        strength = AbilityScore(16, Proficiency.PROFICIENT),
                        dexterity = AbilityScore(12),
                        constitution = AbilityScore(14, Proficiency.PROFICIENT),
                        intelligence = AbilityScore(10),
                        wisdom = AbilityScore(10),
                        charisma = AbilityScore(8),
                    ),
                armorClass = 16,
                maxHitPoints = 12,
                speeds = Speeds(walk = 30),
                languages = listOf(Language.COMMON, Language.DWARVISH),
                classes = mapOf(Character.Class.FIGHTER to 1),
                skills = Skills(),
            )

        fun multiclassBarbarian() =
            humanFighter().copy(
                id = "sample-multiclass",
                name = "Rhogar Sangfroid",
                race = Race.DRAGONBORN,
                classes =
                    mapOf(
                        Character.Class.BARBARIAN to 5,
                        Character.Class.SORCERER to 4,
                        Character.Class.WARLOCK to 3,
                    ),
            )

        fun elfRogue() =
            Character(
                id = "sample-rogue",
                name = "Lyra Vossen",
                race = Race.ELF,
                size = Creature.Size.MEDIUM,
                alignment = Creature.Alignment.CHAOTIC_NEUTRAL,
                abilities =
                    Abilities(
                        strength = AbilityScore(8),
                        dexterity = AbilityScore(16),
                        constitution = AbilityScore(12),
                        intelligence = AbilityScore(14),
                        wisdom = AbilityScore(10),
                        charisma = AbilityScore(14),
                    ),
                armorClass = 14,
                maxHitPoints = 8,
                speeds = Speeds(walk = 30),
                languages = listOf(Language.COMMON, Language.ELVISH),
                classes = mapOf(Character.Class.ROGUE to 1),
                skills = Skills(),
            )
    }
}
