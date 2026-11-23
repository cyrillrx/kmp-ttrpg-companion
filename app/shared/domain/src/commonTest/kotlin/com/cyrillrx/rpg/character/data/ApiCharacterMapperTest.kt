package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.Imported
import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.MAX_CHARACTER_NAME_LENGTH
import com.cyrillrx.rpg.creature.domain.MAX_ARMOR_CLASS
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApiCharacterMapperTest {
    @Test
    fun `a sheet within the rules imports without warning`() {
        assertEquals(emptyList(), importSheet().warnings)
    }

    @Test
    fun `a coerced value is reported with what was declared and what was kept`() {
        val imported = importSheet("armorClass" to JsonPrimitive(99))

        assertEquals(MAX_ARMOR_CLASS, imported.value.armorClass)
        assertEquals(
            listOf(
                CharacterImportWarning.ValueCoerced(
                    SHEET_ID,
                    "armor class",
                    declared = "99",
                    kept = "$MAX_ARMOR_CLASS",
                ),
            ),
            imported.warnings,
        )
    }

    @Test
    fun `a name longer than the maximum is truncated and reported`() {
        val declared = "a".repeat(MAX_CHARACTER_NAME_LENGTH + 1)

        val imported = importSheet("name" to JsonPrimitive(declared))

        assertEquals(
            listOf(
                CharacterImportWarning.ValueCoerced(
                    SHEET_ID,
                    field = "name",
                    declared = declared,
                    kept = "a".repeat(MAX_CHARACTER_NAME_LENGTH),
                ),
            ),
            imported.warnings,
        )
    }

    @Test
    fun `translations are imported trimmed`() {
        val translations = """{"en": {"shortDescription": " Fighter ", "description": " A veteran. "}}"""

        val imported = importSheet("translations" to defaultSerializer.parseToJsonElement(translations))

        assertEquals(
            Character.Translation(shortDescription = "Fighter", description = "A veteran."),
            imported.value.translations["en"],
        )
    }

    @Test
    fun `an empty translation is dropped so another locale can stand in`() {
        val translations = """
            {"en": {"shortDescription": " ", "description": ""}, "fr": {"shortDescription": "Mage", "description": ""}}
        """

        val imported = importSheet("translations" to defaultSerializer.parseToJsonElement(translations))

        assertEquals(setOf("fr"), imported.value.translations.keys)
    }

    @Test
    fun `a sheet with only empty translations imports without any`() {
        val translations = """{"en": {"shortDescription": " ", "description": ""}}"""

        val imported = importSheet("translations" to defaultSerializer.parseToJsonElement(translations))

        assertEquals(emptyMap(), imported.value.translations)
    }

    @Test
    fun `an empty translation does not stand in for an invalid one`() {
        val translations = """{"en": {"shortDescription": " ", "description": ""}, "fr": {"description": ""}}"""

        assertEquals(
            Result.Failure(CharacterImportError.MissingTranslations(SHEET_ID)),
            sheet("translations" to defaultSerializer.parseToJsonElement(translations)).toCharacter(),
        )
    }

    @Test
    fun `an invalid translation is dropped and reported`() {
        val translations = """{"en": {"shortDescription": "Fighter", "description": ""}, "fr": {"description": ""}}"""

        val imported = importSheet("translations" to defaultSerializer.parseToJsonElement(translations))

        assertEquals(setOf("en"), imported.value.translations.keys)
        assertEquals(
            listOf(
                CharacterImportWarning.TranslationDropped(
                    CharacterImportError.InvalidTranslation(SHEET_ID, "fr", "shortDescription"),
                ),
            ),
            imported.warnings,
        )
    }

    @Test
    fun `an unknown background is dropped and reported`() {
        val imported = importSheet("background" to JsonPrimitive("pirate-king"))

        assertNull(imported.value.background)
        assertEquals(listOf(CharacterImportWarning.UnknownBackground(SHEET_ID, "pirate-king")), imported.warnings)
    }

    @Test
    fun `a blank name fails the import`() {
        assertEquals(
            Result.Failure(CharacterImportError.MissingName(SHEET_ID)),
            sheet("name" to JsonPrimitive("  ")).toCharacter(),
        )
    }

    @Test
    fun `a name is imported trimmed`() {
        assertEquals("Thordak", importSheet("name" to JsonPrimitive(" Thordak ")).value.name.value)
    }

    @Test
    fun `every unknown language is named in the failure`() {
        val languages = """["klingon", "common", "atlantean"]"""

        val result = sheet("languages" to defaultSerializer.parseToJsonElement(languages)).toCharacter()

        assertEquals(
            Result.Failure(CharacterImportError.UnknownLanguages(SHEET_ID, listOf("klingon", "atlantean"))),
            result,
        )
    }

    private fun importSheet(vararg overrides: Pair<String, JsonElement>): Imported<Character, CharacterImportWarning> =
        (sheet(*overrides).toCharacter() as Result.Success).value

    private fun sheet(vararg overrides: Pair<String, JsonElement>): ApiCharacter {
        val sheet = defaultSerializer.encodeToJsonElement(SampleCharacterRepository.humanFighter().toApiCharacter())
        return defaultSerializer.decodeFromJsonElement<ApiCharacter>(JsonObject(sheet.jsonObject + overrides))
    }

    private companion object {
        val SHEET_ID = SampleCharacterRepository.humanFighter().id
    }
}
