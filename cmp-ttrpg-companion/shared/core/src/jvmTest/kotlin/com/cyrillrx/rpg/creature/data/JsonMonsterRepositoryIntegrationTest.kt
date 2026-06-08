package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FakeFileReader
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonMonsterRepositoryIntegrationTest {

    @Test
    fun `all monsters from compendium JSON are parsed without silent drops`() = runTest {
        val json = File("../../../data/compendium/monsters.json").readText()
        val rawCount = Json.parseToJsonElement(json).jsonArray.size
        val parsedCount = JsonMonsterRepository(FakeFileReader(json)).getAll(null).size

        assertEquals(rawCount, parsedCount, "Expected $rawCount monsters, got $parsedCount — some failed to parse")
    }
}
