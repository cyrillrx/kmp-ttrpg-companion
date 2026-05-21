package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.domain.FileReaderError
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonCharacterPresetRepositoryTest {
    @Test
    fun `valid preset is parsed with correct field values`() =
        runTest {
            val result = repository(preset()).getAll(null)

            assertEquals(1, result.size)
            val character = result.first()
            assertEquals("test-fighter", character.id)
            assertEquals("Aldus Test", character.name)
            assertEquals(Race.HUMAN, character.race)
            assertEquals(Character.Class.FIGHTER, character.clazz)
            assertEquals(3, character.level)
            assertEquals(Creature.Size.MEDIUM, character.size)
            assertEquals(Creature.Alignment.LAWFUL_GOOD, character.alignment)
            assertEquals(17, character.armorClass)
            assertEquals(28, character.maxHitPoints)
            assertEquals(listOf(Language.COMMON, Language.ELVISH), character.languages)
        }

    @Test
    fun `preset with missing id is skipped`() =
        runTest {
            val json = preset(id = null)
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with missing name is skipped`() =
        runTest {
            val json = preset(name = null)
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with missing translations is skipped`() =
        runTest {
            val json = preset(translations = null)
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with missing level is skipped`() =
        runTest {
            val json = preset(level = null)
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with unknown size is skipped`() =
        runTest {
            val json = preset(size = "GIGANTIC")
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with unknown alignment is skipped`() =
        runTest {
            val json = preset(alignment = "CHAOTIC_POTATO")
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with missing skills is skipped`() =
        runTest {
            val json = preset(skills = null)
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with unknown race is skipped`() =
        runTest {
            val json = preset(race = "MARTIAN")
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with unknown class is skipped`() =
        runTest {
            val json = preset(clazz = "JEDI")
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `abilities and saving throws are parsed correctly`() =
        runTest {
            val json = preset(
                abilities = """{"str": 16, "dex": 12, "con": 14, "int": 10, "wis": 10, "cha": 8}""",
                savingThrows = """{"str": "proficient", "con": "proficient"}""",
            )
            val character = repository(json).getAll(null).first()
            assertEquals(16, character.abilities.strength.value)
            assertEquals(Proficiency.PROFICIENT, character.abilities.strength.savingThrowProficiency)
            assertEquals(12, character.abilities.dexterity.value)
            assertEquals(Proficiency.NONE, character.abilities.dexterity.savingThrowProficiency)
            assertEquals(Proficiency.PROFICIENT, character.abilities.constitution.savingThrowProficiency)
        }

    @Test
    fun `preset with missing walk speed is skipped`() =
        runTest {
            val json = preset(speeds = null)
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `preset with unknown language is skipped`() =
        runTest {
            val json = preset(languages = """["klingon"]""")
            assertTrue(repository(json).getAll(null).isEmpty())
        }

    @Test
    fun `valid records are returned even when some records are invalid`() =
        runTest {
            val json = """[
            ${preset().trimArray()},
            ${preset(id = "second-fighter", race = "MARTIAN").trimArray()}
        ]"""
            val result = repository(json).getAll(null)
            assertEquals(1, result.size)
            assertEquals("test-fighter", result.first().id)
        }

    private fun repository(json: String) = JsonCharacterPresetRepository(FakeFileReader(json), "")

    private fun String.trimArray() = trim().removePrefix("[").removeSuffix("]").trim()

    private fun preset(
        id: String? = "test-fighter",
        name: String? = "Aldus Test",
        race: String? = "human",
        clazz: String? = "fighter",
        level: Int? = 3,
        size: String? = "medium",
        alignment: String? = "lawful_good",
        abilities: String? = null,
        savingThrows: String? = null,
        armorClass: Int? = 17,
        maxHitPoints: Int? = 28,
        speeds: String? = """{"walk": 30}""",
        skills: String? = "{}",
        languages: String? = """["common", "elvish"]""",
        translations: String? = """{"en": {"shortDescription": "Human Fighter", "description": ""}}""",
    ): String {
        val fields =
            buildList {
                id?.let { add(""""id": "$it"""") }
                name?.let { add(""""name": "$it"""") }
                race?.let { add(""""race": "$it"""") }
                clazz?.let { add(""""clazz": "$it"""") }
                level?.let { add(""""level": $it""") }
                size?.let { add(""""size": "$it"""") }
                alignment?.let { add(""""alignment": "$it"""") }
                abilities?.let { add(""""abilities": $it""") }
                savingThrows?.let { add(""""savingThrows": $it""") }
                armorClass?.let { add(""""armorClass": $it""") }
                maxHitPoints?.let { add(""""maxHitPoints": $it""") }
                speeds?.let { add(""""speeds": $it""") }
                skills?.let { add(""""skills": $it""") }
                languages?.let { add(""""languages": $it""") }
                translations?.let { add(""""translations": $it""") }
            }
        return "[{${fields.joinToString(", ")}}]"
    }

    private class FakeFileReader(
        private val json: String,
    ) : FileReader {
        override suspend fun readFile(path: String): Result<String, FileReaderError> = Result.Success(json)
    }
}
