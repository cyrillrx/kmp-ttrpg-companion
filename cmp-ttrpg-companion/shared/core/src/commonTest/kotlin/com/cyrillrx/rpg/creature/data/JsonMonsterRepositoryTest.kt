package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FakeFileReader
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Monster
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonMonsterRepositoryTest {

    @Test
    fun `monster with unaligned alignment is parsed successfully`() = runTest {
        val result = repository(monster(alignment = "unaligned")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(Creature.Alignment.UNALIGNED, result.first().alignment)
    }

    @Test
    fun `monster with any_alignment is parsed successfully`() = runTest {
        val result = repository(monster(alignment = "any_alignment")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(Creature.Alignment.ANY_ALIGNMENT, result.first().alignment)
    }

    @Test
    fun `monster with unknown alignment is skipped`() = runTest {
        assertTrue(repository(monster(alignment = "chaotic_potato")).getAll(null).isEmpty())
    }

    @Test
    fun `monster with missing id is skipped`() = runTest {
        assertTrue(repository(monster(id = null)).getAll(null).isEmpty())
    }

    @Test
    fun `monster with unknown type is skipped`() = runTest {
        assertTrue(repository(monster(type = "dragon_ball")).getAll(null).isEmpty())
    }

    @Test
    fun `monster with swarmer type is skipped`() = runTest {
        assertTrue(repository(monster(type = "swarmer")).getAll(null).isEmpty())
    }

    @Test
    fun `monster with swarm exact type is parsed as SWARM`() = runTest {
        val result = repository(monster(type = "swarm")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(setOf(Monster.Type.SWARM), result.first().types)
    }

    @Test
    fun `monster with swarm prefix type is parsed as SWARM`() = runTest {
        val result = repository(monster(type = "swarm_of_tiny_beasts")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(setOf(Monster.Type.SWARM), result.first().types)
    }

    @Test
    fun `monster with swarm_or composite type resolves as composite not swarm`() = runTest {
        val result = repository(monster(type = "swarm_or_fiend")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(setOf(Monster.Type.SWARM, Monster.Type.FIEND), result.first().types)
    }

    @Test
    fun `monster with composite type resolves all known types`() = runTest {
        val result = repository(monster(type = "celestial_or_fiend")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(setOf(Monster.Type.CELESTIAL, Monster.Type.FIEND), result.first().types)
    }

    @Test
    fun `monster with composite type whose any component is unknown is skipped`() = runTest {
        assertTrue(repository(monster(type = "newtype_or_fiend")).getAll(null).isEmpty())
        assertTrue(repository(monster(type = "fiend_or_newtype")).getAll(null).isEmpty())
        assertTrue(repository(monster(type = "newtype_or_newtype")).getAll(null).isEmpty())
    }

    @Test
    fun `monster with unknown size is skipped`() = runTest {
        assertTrue(repository(monster(size = "gigantic")).getAll(null).isEmpty())
    }

    @Test
    fun `monster with medium_or_small size is parsed as MEDIUM_OR_SMALL`() = runTest {
        val result = repository(monster(size = "medium_or_small")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(Creature.Size.MEDIUM_OR_SMALL, result.first().size)
    }

    @Test
    fun `monster with huge_or_gargantuan size is parsed as HUGE_OR_GARGANTUAN`() = runTest {
        val result = repository(monster(size = "huge_or_gargantuan")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(Creature.Size.HUGE_OR_GARGANTUAN, result.first().size)
    }

    @Test
    fun `monster with huge_or_smaller size is parsed as HUGE_OR_SMALLER`() = runTest {
        val result = repository(monster(size = "huge_or_smaller")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(Creature.Size.HUGE_OR_SMALLER, result.first().size)
    }

    @Test
    fun `valid records are returned even when some records are invalid`() = runTest {
        val result = repository(monster(), monster(id = null)).getAll(null)
        assertEquals(1, result.size)
        assertEquals("test-monster", result.first().id)
    }

    @Test
    fun `monster with an out-of-range armor class is clamped`() = runTest {
        val tooLow = repository(monster(armorClass = 0)).getAll(null).first()
        assertEquals(1, tooLow.armorClass)

        val tooHigh = repository(monster(armorClass = 100)).getAll(null).first()
        assertEquals(40, tooHigh.armorClass)
    }

    @Test
    fun `monster with out-of-range max hit points is clamped`() = runTest {
        val tooLow = repository(monster(maxHitPoints = 0)).getAll(null).first()
        assertEquals(1, tooLow.maxHitPoints)

        val tooHigh = repository(monster(maxHitPoints = 1200)).getAll(null).first()
        assertEquals(999, tooHigh.maxHitPoints)
    }

    @Test
    fun `monster speeds are clamped and rounded to the grid`() = runTest {
        val json = """{"walk": 33, "fly": 9999, "swim": -10}"""

        val speeds = repository(monster(speeds = json)).getAll(null).first().speeds

        assertEquals(35, speeds.walk)
        assertEquals(200, speeds.fly)
        assertEquals(0, speeds.swim)
    }

    @Test
    fun `monster keeps a speed the character range would reject`() = runTest {
        val speeds = repository(monster(speeds = """{"walk": 5, "fly": 150}""")).getAll(null).first().speeds

        assertEquals(5, speeds.walk)
        assertEquals(150, speeds.fly)
    }

    private fun repository(vararg monsters: String) =
        JsonMonsterRepository(FakeFileReader("[${monsters.joinToString(",")}]"))

    private fun monster(
        id: String? = "test-monster",
        source: String? = "test",
        type: String? = "beast",
        size: String? = "medium",
        alignment: String? = "neutral",
        challengeRating: Float? = 1.0f,
        armorClass: Int? = 12,
        maxHitPoints: Int? = 20,
        speeds: String = """{"walk": 30}""",
        translations: String? = """
            {
                "en": {
                    "name": "Test Monster",
                    "subtype": null,
                    "description": "A test.",
                    "senses": "Darkvision 60 ft.",
                    "languages": []
                }
            }""",
    ): String {
        val abilities = """{"str": 10, "dex": 10, "con": 10, "int": 10, "wis": 10, "cha": 10}"""
        val fields = buildList {
            id?.let { add(""""id": "$it"""") }
            source?.let { add(""""source": "$it"""") }
            type?.let { add(""""type": "$it"""") }
            size?.let { add(""""size": "$it"""") }
            alignment?.let { add(""""alignment": "$it"""") }
            challengeRating?.let { add(""""challengeRating": $it""") }
            armorClass?.let { add(""""armorClass": $it""") }
            maxHitPoints?.let { add(""""maxHitPoints": $it""") }
            add(""""abilities": $abilities""")
            add(""""speeds": $speeds""")
            add(""""skills": {}""")
            add(""""damageAffinities": {}""")
            add(""""conditionImmunities": {}""")
            translations?.let { add(""""translations": $it""") }
        }
        return "{${fields.joinToString(", ")}}"
    }
}
