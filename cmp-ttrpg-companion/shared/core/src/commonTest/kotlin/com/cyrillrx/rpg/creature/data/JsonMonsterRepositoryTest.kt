package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.domain.FileReaderError
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.creature.domain.Creature
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
    fun `valid records are returned even when some records are invalid`() = runTest {
        val json = """[
            ${monster().trimArray()},
            ${monster(id = null).trimArray()}
        ]"""
        val result = repository(json).getAll(null)
        assertEquals(1, result.size)
        assertEquals("test-monster", result.first().id)
    }

    private fun repository(json: String) = JsonMonsterRepository(FakeFileReader(json))

    private fun String.trimArray() = trim().removePrefix("[").removeSuffix("]").trim()

    private fun monster(
        id: String? = "test-monster",
        source: String? = "test",
        type: String? = "beast",
        size: String? = "medium",
        alignment: String? = "neutral",
        challengeRating: Float? = 1.0f,
        armorClass: Int? = 12,
        maxHitPoints: Int? = 20,
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
        val speeds = """{"walk": 30}"""
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
        return "[{${fields.joinToString(", ")}}]"
    }

    private class FakeFileReader(private val json: String) : FileReader {
        override suspend fun readFile(path: String): Result<String, FileReaderError> = Result.Success(json)
    }
}
