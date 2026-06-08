package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.core.data.FakeFileReader
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonMagicalItemRepositoryTest {

    @Test
    fun `item with varies rarity is parsed successfully`() = runTest {
        val result = repository(item(rarity = "varies")).getAll(null)

        assertEquals(1, result.size)
        assertEquals(MagicalItem.Rarity.VARIES, result.first().rarity)
    }

    @Test
    fun `item with unknown rarity is skipped`() = runTest {
        assertTrue(repository(item(rarity = "mythic")).getAll(null).isEmpty())
    }

    @Test
    fun `item with missing id is skipped`() = runTest {
        assertTrue(repository(item(id = null)).getAll(null).isEmpty())
    }

    @Test
    fun `item with unknown type is skipped`() = runTest {
        assertTrue(repository(item(type = "trinket")).getAll(null).isEmpty())
    }

    @Test
    fun `valid records are returned even when some records are invalid`() = runTest {
        val json = """[
            ${item().trimArray()},
            ${item(id = "second", rarity = "mythic").trimArray()}
        ]"""
        val result = repository(json).getAll(null)
        assertEquals(1, result.size)
        assertEquals("test-item", result.first().id)
    }

    private fun repository(json: String) = JsonMagicalItemRepository(FakeFileReader(json))

    private fun String.trimArray() = trim().removePrefix("[").removeSuffix("]").trim()

    private fun item(
        id: String? = "test-item",
        source: String? = "test",
        type: String? = "wondrous_item",
        rarity: String? = "rare",
        attunement: Boolean = false,
        translations: String? = """{"en": {"name": "Test Item", "subtype": null, "description": "A test item."}}""",
    ): String {
        val fields = buildList {
            id?.let { add(""""id": "$it"""") }
            source?.let { add(""""source": "$it"""") }
            type?.let { add(""""type": "$it"""") }
            rarity?.let { add(""""rarity": "$it"""") }
            add(""""attunement": $attunement""")
            translations?.let { add(""""translations": $it""") }
        }
        return "[{${fields.joinToString(", ")}}]"
    }
}
