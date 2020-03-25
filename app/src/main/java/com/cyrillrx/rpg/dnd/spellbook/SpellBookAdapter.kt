package com.cyrillrx.rpg.dnd.spellbook

import android.view.ViewGroup
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.template.BaseAdapter

/**
 * @author Cyril Leroux
 *         Created on 13/03/2020.
 */
class SpellBookAdapter : BaseAdapter<SpellBookItemView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SpellBookItemView(parent)

    override fun onBindViewHolder(holder: SpellBookItemView, position: Int) {
        holder.bind(getItem(position) as Spell)
    }
}