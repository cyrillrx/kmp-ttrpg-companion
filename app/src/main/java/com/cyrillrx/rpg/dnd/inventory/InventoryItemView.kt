package com.cyrillrx.rpg.dnd.inventory

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.android.utils.inflate
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.inventory.InventoryItem
import com.cyrillrx.rpg.setHtmlText
import kotlinx.android.synthetic.main.item_inventory.view.*

/**
 * @author Cyril Leroux
 *         Created on 12/03/2020.
 */
class InventoryItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_inventory)) {

    var tvTitle: TextView = itemView.tvTitle
    var tvContent: TextView = itemView.tvContent

    fun bind(inventoryItem: InventoryItem) {

        tvTitle.text = inventoryItem.title
        tvContent.tvContent.setHtmlText(inventoryItem.content)
    }
}