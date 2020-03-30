package com.cyrillrx.rpg.dnd.spellbook

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.android.utils.inflate
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.setHtmlText
import kotlinx.android.synthetic.main.item_spell.view.*

/**
 * @author Cyril Leroux
 *         Created on 13/03/2020.
 */
class SpellBookItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_spell)) {

    var tvTitle: TextView = itemView.tvTitle
    var tvSubtitle: TextView = itemView.tvSubtitle
    var tvContent: TextView = itemView.tvContent

    fun bind(spell: Spell) {

        tvTitle.text = spell.title
        val taxonomy = spell.header.taxonomy
        tvSubtitle.text = "${taxonomy.spell_school.firstOrNull()} de niveau ${taxonomy.spell_level.firstOrNull()}"

        itemView.tvCastingTimeLabel.text = "Durée d'incantation"
        itemView.tvCastingTimeValue.text = spell.casting_time

        itemView.tvRangeLabel.text = "Portée"
        itemView.tvRangeValue.text = spell.range

        itemView.tvComponentsLabel.text = "composantes"
        itemView.tvComponentsValue.text = spell.components

        itemView.tvDurationLabel.text = "Durée"
        itemView.tvDurationValue.text = spell.duration

        tvContent.setHtmlText(spell.content)
    }
}