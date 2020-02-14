package com.cyrillrx.rpg.group

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.android.utils.inflate
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.Character
import kotlinx.android.synthetic.main.item_simple.view.tvTitle

/**
 * @author Cyril Leroux
 *         Created on 14/02/2020.
 */
class CharacterItemView(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_simple)) {

    fun bind(character: Character) {
        itemView.tvTitle.text = character.name
    }
}