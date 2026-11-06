package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.Imported
import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Character
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
