package com.cyrillrx.rpg.group

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.android.utils.inflate
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.Character

/**
 * @author Cyril Leroux
 *         Created on 14/02/2020.
 */
class CharacterItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_simple)) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    fun bind(character: Character) {
        tvTitle.text = character.name
    }
}
