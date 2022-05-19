package com.cyrillrx.rpg.dnd.inventory

import android.view.ViewGroup
import com.cyrillrx.rpg.api.inventory.InventoryItem
import com.cyrillrx.rpg.template.BaseAdapter

class InventoryAdapter : BaseAdapter<InventoryItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InventoryItemView(parent)

    override fun onBindViewHolder(holder: InventoryItemView, position: Int) {
        holder.bind(getItem(position) as InventoryItem)
    }
}
