package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.DEFAULT_CLASS_LEVELS
import com.cyrillrx.rpg.character.domain.totalLevel
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterMigrationTest {

    @Test
    fun `legacy clazz and level are migrated to a single class entry`() {
        val migrated = legacyJson(clazz = "FIGHTER", level = 4).deserializeCharacter()
        assertEquals(mapOf(Character.Class.FIGHTER to 4), migrated.classes)
    }

    @Test
    fun `legacy UNKNOWN class keeps its level`() {
        val migrated = legacyJson(clazz = "UNKNOWN", level = 2).deserializeCharacter()
        assertEquals(mapOf(Character.Class.UNKNOWN to 2), migrated.classes)
    }

    @Test
    fun `a record carrying no class information at all falls back to an unspecified class`() {
        val migrated = jsonWithout("classes").deserializeCharacter()
        assertEquals(DEFAULT_CLASS_LEVELS, migrated.classes)
    }

    @Test
    fun `an explicitly empty class map falls back to an unspecified class`() {
        val migrated = SampleCharacterRepository.humanFighter()
            .copy(classes = emptyMap())
            .serialize()
            .deserializeCharacter()
        assertEquals(DEFAULT_CLASS_LEVELS, migrated.classes)
    }

    @Test
    fun `modern classes are left untouched`() {
        val modern = SampleCharacterRepository.multiclassBarbarian()
        assertEquals(modern.classes, modern.serialize().deserializeCharacter().classes)
        assertEquals(12, modern.totalLevel)
    }

    private fun legacyJson(clazz: String, level: Int): String = buildJsonObject {
        objectWithout("classes").forEach { (key, value) -> put(key, value) }
        put("clazz", JsonPrimitive(clazz))
        put("level", JsonPrimitive(level))
    }.toString()

    private fun jsonWithout(vararg keys: String): String = objectWithout(*keys).toString()

    private fun objectWithout(vararg keys: String): JsonObject {
        val modern = defaultSerializer.parseToJsonElement(
            SampleCharacterRepository.humanFighter().serialize(),
        ).jsonObject
        return buildJsonObject {
            modern.forEach { (key, value) -> if (key !in keys) put(key, value) }
        }
    }
}
