package com.cyrillrx.rpg.xml.inventory

import android.view.ViewGroup
import com.cyrillrx.rpg.api.inventory.ApiInventoryItem
import com.cyrillrx.rpg.xml.template.BaseAdapter

class InventoryAdapter : BaseAdapter<InventoryItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InventoryItemView(parent)

    override fun onBindViewHolder(holder: InventoryItemView, position: Int) {
        holder.bind(getItem(position) as ApiInventoryItem)
    }
}
