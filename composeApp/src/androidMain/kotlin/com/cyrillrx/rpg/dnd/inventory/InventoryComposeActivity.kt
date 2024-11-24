package com.cyrillrx.rpg.dnd.inventory

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.inventory.InventoryItem
import com.cyrillrx.rpg.api.inventory.MagicalItem
import com.cyrillrx.rpg.presentation.theme.AppTheme
import com.cyrillrx.utils.deserialize

class InventoryComposeActivity : AppCompatActivity() {

    private val viewModel by viewModels<InventoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                InventoryScreen(viewModel.magicalItems, viewModel.query, viewModel::applyFilter)
            }
        }

        viewModel.init(loadFromFile())
    }


    private fun loadFromFile(): List<MagicalItem> {
        val serializedInventory = AssetReader.readAsString(this, "objets-magiques.json")
        val inventoryItems: List<InventoryItem> = serializedInventory?.deserialize() ?: listOf()
        return inventoryItems.map { it.toMagicalItem() }
    }

    companion object {
        private fun InventoryItem.toMagicalItem(): MagicalItem {
            return MagicalItem(
                title = title,
                subtitle = getSubtitle(),
                description = content,
                type = getType(),
                rarety = getRarety(),
                attunement = attunement != null,
            )
        }

        private fun InventoryItem.getSubtitle(): String {
            val attunementString = attunement?.let { " ($attunement)" } ?: ""
            return "$type, $rarity$attunementString"
        }

        private fun InventoryItem.getType(): MagicalItem.Type = when (type) {
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

        private fun InventoryItem.getRarety(): MagicalItem.Rarety = when (rarity) {
            "Courant" -> MagicalItem.Rarety.COMMON
            "Peu courant" -> MagicalItem.Rarety.UNCOMMON
            "Rare" -> MagicalItem.Rarety.RARE
            "Très rare" -> MagicalItem.Rarety.VERY_RARE
            "Légendaire" -> MagicalItem.Rarety.LEGENDARY
            "Artefact" -> MagicalItem.Rarety.ARTIFACT
            else -> MagicalItem.Rarety.UNCOMMON
        }
    }
}
