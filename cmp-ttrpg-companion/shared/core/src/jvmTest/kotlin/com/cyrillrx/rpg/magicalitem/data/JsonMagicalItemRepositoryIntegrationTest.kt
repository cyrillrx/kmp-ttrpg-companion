package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.domain.FileReaderError
import com.cyrillrx.core.domain.Result
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonMagicalItemRepositoryIntegrationTest {

    @Test
    fun `all magical items from compendium JSON are parsed without silent drops`() = runTest {
        val json = File("../../../data/compendium/magical-items.json").readText()
        val rawCount = Json.parseToJsonElement(json).jsonArray.size
        val parsedCount = JsonMagicalItemRepository(FakeFileReader(json)).getAll(null).size

        assertEquals(rawCount, parsedCount, "Expected $rawCount items, got $parsedCount — some failed to parse")
    }

    private class FakeFileReader(private val json: String) : FileReader {
        override suspend fun readFile(path: String): Result<String, FileReaderError> = Result.Success(json)
    }
}
