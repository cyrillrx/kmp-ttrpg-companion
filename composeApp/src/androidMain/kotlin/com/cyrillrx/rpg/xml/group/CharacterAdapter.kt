package com.cyrillrx.rpg.xml.group

import android.view.ViewGroup
import com.cyrillrx.rpg.models.Character
import com.cyrillrx.rpg.template.BaseAdapter

class CharacterAdapter : BaseAdapter<CharacterItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemView = CharacterItemView(parent)

    override fun onBindViewHolder(holder: CharacterItemView, position: Int) {
        holder.bind(getItem(position) as Character)
    }
}
