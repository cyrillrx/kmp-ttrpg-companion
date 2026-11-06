package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class MonsterProficiencyBonusTest {

    @Test
    fun `proficiency bonus follows the 5e challenge-rating table`() {
        // Each pair is (challenge rating, expected proficiency bonus), covering the lower
        // and upper edge of every tier plus the fractional-CR case.
        val expectations = listOf(
            0f to 2,
            0.25f to 2,
            4f to 2,
            5f to 3,
            8f to 3,
            9f to 4,
            12f to 4,
            13f to 5,
            16f to 5,
            17f to 6,
            20f to 6,
            21f to 7,
            24f to 7,
            25f to 8,
            28f to 8,
            29f to 9,
            30f to 9,
        )

        expectations.forEach { (challengeRating, expectedBonus) ->
            assertEquals(
                expectedBonus,
                monsterWithChallengeRating(challengeRating).proficiencyBonus(),
                "CR $challengeRating should yield a proficiency bonus of $expectedBonus",
            )
        }
    }

    private fun monsterWithChallengeRating(challengeRating: Float): Monster {
        val score = AbilityScore(value = AbilityScore.DEFAULT_VALUE)
        return Monster(
            id = "test",
            size = Creature.Size.MEDIUM,
            alignment = Creature.Alignment.UNALIGNED,
            abilities = Abilities(score, score, score, score, score, score),
            armorClass = 10,
            maxHitPoints = 1,
            speeds = Speeds(walk = 30),
            source = "test",
            types = setOf(Monster.Type.HUMANOID),
            challengeRating = challengeRating,
            hitDice = "1d8",
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Test",
                    description = "",
                    senses = "",
                    languages = emptyList(),
                ),
            ),
        )
    }
}
