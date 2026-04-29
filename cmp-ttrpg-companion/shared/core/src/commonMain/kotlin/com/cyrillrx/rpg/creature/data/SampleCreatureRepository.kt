package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.applyFilter

class SampleCreatureRepository : CreatureRepository {
    override suspend fun getAll(filter: CreatureFilter?): List<Monster> {
        return creatures.applyFilter(filter)
    }

    override suspend fun getById(id: String): Monster? = creatures.firstOrNull { it.id == id }

    companion object {
        private val creatures: List<Monster> = listOf(
            goblin(),
            youngRedDragon(),
            skeleton(),
            direWolf(),
            balor(),
            gelatinousCube(),
        )

        fun getAll(): List<Monster> = creatures

        fun getFirst(): Monster = creatures.first()

        fun goblin() = Monster(
            id = "1",
            name = "Goblin",
            description = "A small, black-hearted creature that lairs in despoiled dungeons and other dismal settings.",
            source = "srd_5.1",
            type = Monster.Type.HUMANOID,
            size = Creature.Size.SMALL,
            alignment = Creature.Alignment.NEUTRAL_EVIL,
            challengeRating = 0.25f,
            hitDice = "2d6",
            abilities = Abilities(
                str = Ability(8),
                dex = Ability(14),
                con = Ability(10),
                int = Ability(10),
                wis = Ability(8),
                cha = Ability(8),
            ),
            armorClass = 15,
            maxHitPoints = 7,
            speed = "30 ft.",
            languages = listOf("Common", "Goblin"),
        )

        fun youngRedDragon() = Monster(
            id = "2",
            name = "Young Red Dragon",
            description = "A fierce dragon that breathes fire and terrorizes the countryside.",
            source = "srd_5.1",
            type = Monster.Type.DRAGON,
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 10f,
            hitDice = "17d10+85",
            abilities = Abilities(
                str = Ability(23),
                dex = Ability(10),
                con = Ability(21),
                int = Ability(14),
                wis = Ability(11),
                cha = Ability(19),
            ),
            armorClass = 18,
            maxHitPoints = 178,
            speed = "40 ft., climb 40 ft., fly 80 ft.",
            languages = listOf("Common", "Draconic"),
        )

        private fun skeleton() = Monster(
            id = "3",
            name = "Skeleton",
            description = "An animated pile of bones held together by dark magic.",
            source = "srd_5.1",
            type = Monster.Type.UNDEAD,
            size = Creature.Size.MEDIUM,
            alignment = Creature.Alignment.LAWFUL_EVIL,
            challengeRating = 0.25f,
            hitDice = "2d8+4",
            abilities = Abilities(
                str = Ability(10),
                dex = Ability(14),
                con = Ability(15),
                int = Ability(6),
                wis = Ability(8),
                cha = Ability(5),
            ),
            armorClass = 13,
            maxHitPoints = 13,
            speed = "30 ft.",
            languages = listOf("understands languages it knew in life"),
        )

        private fun direWolf() = Monster(
            id = "4",
            name = "Dire Wolf",
            description = "A large and fearsome wolf that hunts in packs.",
            source = "srd_5.1",
            type = Monster.Type.BEAST,
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.NEUTRAL,
            challengeRating = 1f,
            hitDice = "5d10+10",
            abilities = Abilities(
                str = Ability(17),
                dex = Ability(15),
                con = Ability(15),
                int = Ability(3),
                wis = Ability(12),
                cha = Ability(7),
            ),
            armorClass = 14,
            maxHitPoints = 37,
            speed = "50 ft.",
            languages = emptyList(),
        )

        private fun balor() = Monster(
            id = "5",
            name = "Balor",
            description = "A towering fiend wreathed in flame, wielding a whip and longsword of fire.",
            source = "srd_5.1",
            type = Monster.Type.FIEND,
            size = Creature.Size.HUGE,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 19f,
            hitDice = "19d12+114",
            abilities = Abilities(
                str = Ability(26),
                dex = Ability(15),
                con = Ability(22),
                int = Ability(20),
                wis = Ability(16),
                cha = Ability(22),
            ),
            armorClass = 19,
            maxHitPoints = 262,
            speed = "40 ft., fly 80 ft.",
            languages = listOf("Abyssal", "telepathy 120 ft."),
        )

        private fun gelatinousCube() = Monster(
            id = "6",
            name = "Gelatinous Cube",
            description = "A nearly transparent ooze that fills dungeon corridors.",
            source = "srd_5.1",
            type = Monster.Type.OOZE,
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.NEUTRAL,
            challengeRating = 2f,
            hitDice = "8d10+40",
            abilities = Abilities(
                str = Ability(14),
                dex = Ability(3),
                con = Ability(20),
                int = Ability(1),
                wis = Ability(6),
                cha = Ability(1),
            ),
            armorClass = 6,
            maxHitPoints = 84,
            speed = "15 ft.",
            languages = emptyList(),
        )
    }
}
