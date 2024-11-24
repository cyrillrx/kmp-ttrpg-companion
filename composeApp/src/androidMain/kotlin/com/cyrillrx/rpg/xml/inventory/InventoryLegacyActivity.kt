package com.cyrillrx.rpg.xml.inventory

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.inventory.InventoryItem
import com.cyrillrx.rpg.template.SearchListActivity
import com.cyrillrx.utils.deserialize
import java.util.Locale

class InventoryLegacyActivity : SearchListActivity<InventoryAdapter>() {

    override val adapter = InventoryAdapter()
    private val locale by lazy { Locale.ROOT }

    override fun createLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    override fun sendRequest() {

        val magicalItems = loadFromFile()
        updateData(magicalItems)
    }

    override fun applyFilter(query: String) {

        val magicalItems = loadFromFile()
        updateData(magicalItems.filter(query))
    }

    private fun updateData(magicalItems: List<InventoryItem>) {

        startLoading()

        adapter.clear()
        adapter.addAll(magicalItems)

        stopLoading()
    }

    private fun loadFromFile(): List<InventoryItem> {
        val serializedInventory = AssetReader.readAsString(this, "objets-magiques.json")
        return serializedInventory?.deserialize() ?: listOf()
    }

    private fun List<InventoryItem>.filter(query: String): List<InventoryItem> =
        filterTo(arrayListOf()) { item -> item.filter(query) }

    private fun InventoryItem.filter(query: String): Boolean {
        val lowerCaseQuery = query.trim().lowercase(locale)
        return title.lowercase(locale).contains(lowerCaseQuery) ||
                content.lowercase(locale).contains(lowerCaseQuery)
    }
}
