package com.cyrillrx.rpg.xml.bestiary

import android.view.ViewGroup
import com.cyrillrx.rpg.api.bestiary.BestiaryItem
import com.cyrillrx.rpg.xml.template.BaseAdapter

class BestiaryAdapter : BaseAdapter<BestiaryItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BestiaryItemView(parent)

    override fun onBindViewHolder(holder: BestiaryItemView, position: Int) {
        holder.bind(getItem(position) as BestiaryItem)
    }
}
