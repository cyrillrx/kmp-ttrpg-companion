package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.magicalitem.data.api.ApiInventoryItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemFilter
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.applyFilter

class JsonMagicalItemRepository(private val fileReader: FileReader) : MagicalItemRepository {

    private var cache: List<MagicalItem>? = null

    override suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem> {
        val allItems = cache ?: loadAndParse()
        return allItems.applyFilter(filter)
    }

    override suspend fun getById(id: String): MagicalItem? =
        getAll(null).firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<MagicalItem> {
        val all = getAll(null).associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

    private suspend fun loadAndParse(): List<MagicalItem> {
        val apiItems = loadFromFile()
        val results = apiItems.map { it.toMagicalItem() }

        val failures = results.filterIsInstance<Result.Failure<MagicalItemImportError>>()
        val successes = results.filterIsInstance<Result.Success<MagicalItem>>()
        failures.forEach { println("WARNING: magical item import error: ${it.error}") }
        return successes.map { it.value }.also { cache = it }
    }

    private suspend fun loadFromFile(): List<ApiInventoryItem> {
        val result = fileReader.readFile("files/magical-items.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private typealias ItemResult = Result<MagicalItem, MagicalItemImportError>

        private fun ApiInventoryItem.toMagicalItem(): ItemResult {
            val id = id
                ?: return Result.Failure(MagicalItemImportError.MissingId)
            val source = source
                ?: return Result.Failure(MagicalItemImportError.MissingSource(id))
            val apiType = type
                ?: return Result.Failure(MagicalItemImportError.MissingType(id))
            val type = apiType.toType()
                ?: return Result.Failure(MagicalItemImportError.UnknownType(id, apiType))
            val apiRarity = rarity
                ?: return Result.Failure(MagicalItemImportError.MissingRarity(id))
            val rarity = apiRarity.toRarity()
                ?: return Result.Failure(MagicalItemImportError.UnknownRarity(id, apiRarity))
            val apiTranslations = translations
                ?: return Result.Failure(MagicalItemImportError.MissingTranslations(id))
            val translations = apiTranslations
                .mapNotNull { (locale, t) ->
                    when (val result = t.toDomain(id, locale)) {
                        is Result.Success -> locale to result.value
                        is Result.Failure -> {
                            println("WARNING: magical item import error: ${result.error}")
                            null
                        }
                    }
                }
                .toMap()
                .takeIf { it.isNotEmpty() }
                ?: return Result.Failure(MagicalItemImportError.MissingTranslations(id))

            return Result.Success(
                MagicalItem(
                    id = id,
                    source = source,
                    type = type,
                    rarity = rarity,
                    attunement = attunement ?: false,
                    translations = translations,
                )
            )
        }

        private fun ApiInventoryItem.Translation.toDomain(
            itemId: String,
            locale: String,
        ): Result<MagicalItem.Translation, MagicalItemImportError> {
            val name = name ?: return Result.Failure(MagicalItemImportError.InvalidTranslation(itemId, locale))
            val description = description ?: return Result.Failure(MagicalItemImportError.InvalidTranslation(itemId, locale))
            return Result.Success(
                MagicalItem.Translation(
                    name = name,
                    subtype = subtype,
                    description = description,
                )
            )
        }

        private fun String.toType(): MagicalItem.Type? =
            MagicalItem.Type.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toRarity(): MagicalItem.Rarity? =
            MagicalItem.Rarity.entries.find { it.name.equals(this, ignoreCase = true) }
    }
}
