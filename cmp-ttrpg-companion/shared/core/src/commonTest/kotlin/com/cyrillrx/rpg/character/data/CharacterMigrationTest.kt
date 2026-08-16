package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.defaultSerializer
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.ClassLevel
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharacterMigrationTest {

    @Test
    fun `legacy clazz and level are migrated to a single class entry`() {
        val migrated = legacyJson(clazz = "FIGHTER", level = 4).deserializeCharacter()
        assertEquals(listOf(ClassLevel(Character.Class.FIGHTER, 4)), migrated.classes)
    }

    @Test
    fun `legacy UNKNOWN class becomes no class`() {
        val migrated = legacyJson(clazz = "UNKNOWN", level = 2).deserializeCharacter()
        assertTrue(migrated.classes.isEmpty())
    }

    @Test
    fun `modern classes are left untouched`() {
        val modern = SampleCharacterRepository.humanFighter()
            .copy(classes = listOf(ClassLevel(Character.Class.ROGUE, 3), ClassLevel(Character.Class.WIZARD, 2)))
        assertEquals(modern.classes, modern.serialize().deserializeCharacter().classes)
    }

    /** A stored character in the legacy single-class shape (`clazz` + `level`, no `classes`). */
    private fun legacyJson(clazz: String, level: Int): String {
        val modern = defaultSerializer.parseToJsonElement(
            SampleCharacterRepository.humanFighter().serialize(),
        ).jsonObject
        return buildJsonObject {
            modern.forEach { (key, value) -> if (key != "classes") put(key, value) }
            put("clazz", JsonPrimitive(clazz))
            put("level", JsonPrimitive(level))
        }.toString()
    }
}
