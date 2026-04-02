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
        val allItems = cache ?: loadFromFile()
            .map { it.toMagicalItem() }
            .also { cache = it }

        return allItems.applyFilter(filter)
    }

    override suspend fun getById(id: String): MagicalItem? =
        getAll(null).firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<MagicalItem> {
        val all = getAll(null).associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

    private suspend fun loadFromFile(): List<ApiInventoryItem> {
        val result = fileReader.readFile("files/objets-magiques.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun ApiInventoryItem.toMagicalItem(): MagicalItem {
            return MagicalItem(
                id = title,
                title = title,
                subtitle = getSubtitle(),
                description = content,
                type = getType(),
                rarity = getRarity(),
                attunement = attunement != null,
            )
        }

        private fun ApiInventoryItem.getSubtitle(): String {
            val attunementString = attunement?.let { " ($attunement)" } ?: ""
            return "$type, $rarity$attunementString"
        }

        private fun ApiInventoryItem.getType(): MagicalItem.Type = when (type) {
            "Armure" -> MagicalItem.Type.ARMOR
            "Potion" -> MagicalItem.Type.POTION
            "Anneau" -> MagicalItem.Type.RING
            "Septre" -> MagicalItem.Type.ROD
            "Parchemin" -> MagicalItem.Type.SCROLL
            "Bâton" -> MagicalItem.Type.STAFF
            "Baguette" -> MagicalItem.Type.WAND
            "Arme" -> MagicalItem.Type.WEAPON
            "Objet merveilleux" -> MagicalItem.Type.WONDROUS_ITEM
            else -> MagicalItem.Type.WONDROUS_ITEM
        }

        private fun ApiInventoryItem.getRarity(): MagicalItem.Rarity = when (rarity) {
            "Courant" -> MagicalItem.Rarity.COMMON
            "Peu courant" -> MagicalItem.Rarity.UNCOMMON
            "Rare" -> MagicalItem.Rarity.RARE
            "Très rare" -> MagicalItem.Rarity.VERY_RARE
            "Légendaire" -> MagicalItem.Rarity.LEGENDARY
            "Artefact" -> MagicalItem.Rarity.ARTIFACT
            else -> MagicalItem.Rarity.UNCOMMON
        }
    }
}
