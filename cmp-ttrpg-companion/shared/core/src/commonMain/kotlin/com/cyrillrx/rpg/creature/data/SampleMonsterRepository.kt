package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Speeds
import com.cyrillrx.rpg.creature.domain.applyFilter

class SampleMonsterRepository : MonsterRepository {
    override suspend fun getAll(filter: MonsterFilter?): List<Monster> {
        return monsters.applyFilter(filter)
    }

    override suspend fun getById(id: String): Monster? = monsters.firstOrNull { it.id == id }

    companion object {
        private val monsters: List<Monster> = listOf(
            goblin(),
            youngRedDragon(),
            skeleton(),
            direWolf(),
            balor(),
            gelatinousCube(),
        )

        fun getAll(): List<Monster> = monsters

        fun getFirst(): Monster = monsters.first()

        fun goblin() = Monster(
            id = "1",
            source = "srd_5.1",
            type = Monster.Type.HUMANOID,
            size = Creature.Size.SMALL,
            alignment = Creature.Alignment.NEUTRAL_EVIL,
            challengeRating = 0.25f,
            hitDice = "2d6",
            abilities = Abilities(
                strength = Ability(8),
                dexterity = Ability(14),
                constitution = Ability(10),
                intelligence = Ability(10),
                wisdom = Ability(8),
                charisma = Ability(8),
            ),
            armorClass = 15,
            maxHitPoints = 7,
            speeds = Speeds(walk = 30),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Goblin",
                    description = "A small, black-hearted creature that lairs in despoiled dungeons and other " +
                        "dismal settings.",
                    senses = "Darkvision 60 ft.",
                    languages = listOf("Common", "Goblin"),
                ),
            ),
        )

        fun youngRedDragon() = Monster(
            id = "2",
            source = "srd_5.1",
            type = Monster.Type.DRAGON,
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 10f,
            hitDice = "17d10+85",
            abilities = Abilities(
                strength = Ability(23),
                dexterity = Ability(10),
                constitution = Ability(21),
                intelligence = Ability(14),
                wisdom = Ability(11),
                charisma = Ability(19),
            ),
            armorClass = 18,
            maxHitPoints = 178,
            speeds = Speeds(walk = 40, climb = 40, fly = 80),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Young Red Dragon",
                    description = "A fierce dragon that breathes fire and terrorizes the countryside.",
                    senses = "Blindsight 30 ft., Darkvision 120 ft.",
                    languages = listOf("Common", "Draconic"),
                ),
            ),
        )

        private fun skeleton() = Monster(
            id = "3",
            source = "srd_5.1",
            type = Monster.Type.UNDEAD,
            size = Creature.Size.MEDIUM,
            alignment = Creature.Alignment.LAWFUL_EVIL,
            challengeRating = 0.25f,
            hitDice = "2d8+4",
            abilities = Abilities(
                strength = Ability(10),
                dexterity = Ability(14),
                constitution = Ability(15),
                intelligence = Ability(6),
                wisdom = Ability(8),
                charisma = Ability(5),
            ),
            armorClass = 13,
            maxHitPoints = 13,
            speeds = Speeds(walk = 30),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Skeleton",
                    description = "An animated pile of bones held together by dark magic.",
                    senses = "Darkvision 60 ft.",
                    languages = listOf("understands languages it knew in life"),
                ),
            ),
        )

        private fun direWolf() = Monster(
            id = "4",
            source = "srd_5.1",
            type = Monster.Type.BEAST,
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.NEUTRAL,
            challengeRating = 1f,
            hitDice = "5d10+10",
            abilities = Abilities(
                strength = Ability(17),
                dexterity = Ability(15),
                constitution = Ability(15),
                intelligence = Ability(3),
                wisdom = Ability(12),
                charisma = Ability(7),
            ),
            armorClass = 14,
            maxHitPoints = 37,
            speeds = Speeds(walk = 50),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Dire Wolf",
                    description = "A large and fearsome wolf that hunts in packs.",
                    senses = "Passive Perception 13",
                    languages = emptyList(),
                ),
            ),
        )

        private fun balor() = Monster(
            id = "5",
            source = "srd_5.1",
            type = Monster.Type.FIEND,
            size = Creature.Size.HUGE,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 19f,
            hitDice = "19d12+114",
            abilities = Abilities(
                strength = Ability(26),
                dexterity = Ability(15),
                constitution = Ability(22),
                intelligence = Ability(20),
                wisdom = Ability(16),
                charisma = Ability(22),
            ),
            armorClass = 19,
            maxHitPoints = 262,
            speeds = Speeds(walk = 40, fly = 80),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Balor",
                    description = "A towering fiend wreathed in flame, wielding a whip and longsword of fire.",
                    senses = "Truesight 120 ft.",
                    languages = listOf("Abyssal", "telepathy 120 ft."),
                ),
            ),
        )

        private fun gelatinousCube() = Monster(
            id = "6",
            source = "srd_5.1",
            type = Monster.Type.OOZE,
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.NEUTRAL,
            challengeRating = 2f,
            hitDice = "8d10+40",
            abilities = Abilities(
                strength = Ability(14),
                dexterity = Ability(3),
                constitution = Ability(20),
                intelligence = Ability(1),
                wisdom = Ability(6),
                charisma = Ability(1),
            ),
            armorClass = 6,
            maxHitPoints = 84,
            speeds = Speeds(walk = 15),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Gelatinous Cube",
                    description = "A nearly transparent ooze that fills dungeon corridors.",
                    senses = "Blindsight 60 ft.",
                    languages = emptyList(),
                ),
            ),
        )
    }
}
