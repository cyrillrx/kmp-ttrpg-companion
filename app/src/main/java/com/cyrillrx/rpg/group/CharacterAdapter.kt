package com.cyrillrx.rpg.group

import android.view.ViewGroup
import com.cyrillrx.rpg.models.Character
import com.cyrillrx.rpg.template.BaseAdapter

/**
 * @author Cyril Leroux
 *         Created on 14/02/2020.
 */
class CharacterAdapter : BaseAdapter<CharacterItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemView = CharacterItemView(parent)

    override fun onBindViewHolder(holder: CharacterItemView, position: Int) {
        holder.bind(getItem(position) as Character)
    }
}