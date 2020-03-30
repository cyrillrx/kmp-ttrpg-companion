package com.cyrillrx.rpg.dnd.inventory

import android.os.Bundle
import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.inventory.InventoryItem
import com.cyrillrx.rpg.template.ListActivity
import com.cyrillrx.utils.deserialize
import kotlinx.android.synthetic.main.activity_list.*

/**
 * @author Cyril Leroux
 *         Created on 30/03/2020.
 */
class InventoryActivity : ListActivity<InventoryAdapter>() {

    override val adapter = InventoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
    }

    override fun sendRequest() {

        startLoading()

        val serializedInventory = AssetReader.readAsString(this, "objets-magiques.json")
        val magicalItems: Inventory = serializedInventory?.deserialize() ?: Inventory()

        adapter.addAll(magicalItems)

        stopLoading()
    }

    class Inventory : ArrayList<InventoryItem>()
}