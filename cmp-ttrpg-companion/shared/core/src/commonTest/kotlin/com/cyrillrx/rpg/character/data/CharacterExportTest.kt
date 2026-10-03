package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.data.api.ApiCharacterExport
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import kotlinx.datetime.TimeZone
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class CharacterExportTest {
    private val exportedAt = Instant.parse("2026-09-19T10:24:00Z")

    @Test
    fun `an exported sheet reads back identical`() {
        val original = richCharacter()

        assertEquals(original, original.exportAndReadBack())
    }

    @Test
    fun `a sheet with no translation reads back identical`() {
        val original = SampleCharacterRepository.humanFighter()

        assertEquals(original, original.exportAndReadBack())
    }

    @Test
    fun `the primary class survives even when declared after another one`() {
        val original = richCharacter()

        assertEquals(Character.Class.WIZARD, original.exportAndReadBack().primaryClass)
    }

    @Test
    fun `the envelope carries every required field`() {
        val content = richCharacter().toExportFile(APP_VERSION, exportedAt, PARIS).content

        val envelope = content.deserialize<ApiCharacterExport>()

        assertEquals(1, envelope.formatVersion)
        assertEquals("character", envelope.entityType)
        assertEquals(APP_VERSION, envelope.appVersion)
        assertEquals("2026-09-19T10:24:00Z", envelope.exportedAt)
        assertEquals("sample-archmage", envelope.sourceId)
        assertEquals(
            setOf("formatVersion", "entityType", "appVersion", "exportedAt", "sourceId", "character"),
            Json.parseToJsonElement(content).jsonObject.keys,
        )
    }

    @Test
    fun `the file is written as readable JSON`() {
        val file = richCharacter().toExportFile(APP_VERSION, exportedAt, PARIS)

        assertEquals("application/json", file.mimeType)
        assertTrue(file.content.lines().size > 1)
    }

    @Test
    fun `values are written in the preset vocabulary`() {
        val content = richCharacter().toExportFile(APP_VERSION, exportedAt, PARIS).content

        assertTrue(""""race": "half_elf"""" in content)
        assertTrue(""""alignment": "chaotic_good"""" in content)
        assertTrue(""""background": "sage"""" in content)
        assertTrue(""""arcana": "expert"""" in content)
    }

    @Test
    fun `the file is named after the character its level and the export date`() {
        assertEquals(
            "élowen-brume_lvl9_2026-09-19.character.json",
            richCharacter().toExportFile(APP_VERSION, exportedAt, PARIS).name,
        )
    }

    @Test
    fun `the export date is the local one`() {
        val lateEvening = Instant.parse("2026-09-19T22:30:00Z")

        assertTrue(
            richCharacter().toExportFile(APP_VERSION, lateEvening, PARIS).name.endsWith("_2026-09-20.character.json"),
        )
    }

    private fun Character.exportAndReadBack(): Character {
        val envelope = toExportFile(APP_VERSION, exportedAt, PARIS).content.deserialize<ApiCharacterExport>()
        val result = requireNotNull(envelope.character).toCharacter()
        return (result as Result.Success).value.value
    }

    private fun richCharacter(): Character = SampleCharacterRepository.humanFighter().copy(
        id = "sample-archmage",
        name = "Élowen Brume",
        race = Race.HALF_ELF,
        alignment = Creature.Alignment.CHAOTIC_GOOD,
        background = Background.SAGE,
        classes = mapOf(Character.Class.FIGHTER to 2, Character.Class.WIZARD to 7),
        primaryClass = Character.Class.WIZARD,
        currentHitPoints = 5,
        temporaryHitPoints = 3,
        speeds = Speeds(walk = 30, fly = 60, hover = true),
        languages = listOf(Language.COMMON, Language.ELVISH, Language.DRACONIC),
        skills = Skills(arcana = Proficiency.EXPERT, history = Proficiency.PROFICIENT),
        translations = mapOf(
            "en" to Character.Translation(shortDescription = "Guild archmage", description = "Keeps the tower."),
            "fr" to Character.Translation(shortDescription = "Archimage de la guilde", description = ""),
        ),
    )

    private companion object {
        const val APP_VERSION = "1.4.0"
        val PARIS = TimeZone.of("Europe/Paris")
    }
}
