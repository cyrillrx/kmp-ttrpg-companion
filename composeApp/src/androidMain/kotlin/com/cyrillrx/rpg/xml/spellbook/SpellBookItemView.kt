package com.cyrillrx.rpg.xml.spellbook

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.setHtmlText
import com.cyrillrx.rpg.xml.inflate

class SpellBookItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_spell)) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private var tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
    private var tvContent: TextView = itemView.findViewById(R.id.tvContent)

    private var tvCastingTimeLabel: TextView = itemView.findViewById(R.id.tvCastingTimeLabel)
    private var tvCastingTimeValue: TextView = itemView.findViewById(R.id.tvCastingTimeValue)
    private var tvRangeLabel: TextView = itemView.findViewById(R.id.tvRangeLabel)
    private var tvRangeValue: TextView = itemView.findViewById(R.id.tvRangeValue)
    private var tvComponentsLabel: TextView = itemView.findViewById(R.id.tvComponentsLabel)
    private var tvComponentsValue: TextView = itemView.findViewById(R.id.tvComponentsValue)
    private var tvDurationLabel: TextView = itemView.findViewById(R.id.tvDurationLabel)
    private var tvDurationValue: TextView = itemView.findViewById(R.id.tvDurationValue)

    fun bind(spell: Spell) {
        tvTitle.text = spell.title
        tvSubtitle.text = tvSubtitle.context.getString(
            R.string.formatted_spell_school_level,
            spell.getSchool(),
            spell.level,
        )

        tvCastingTimeLabel.text = "Durée d'incantation"
        tvCastingTimeValue.text = spell.casting_time

        tvRangeLabel.text = "Portée"
        tvRangeValue.text = spell.range

        tvComponentsLabel.text = "composantes"
        tvComponentsValue.text = spell.components

        tvDurationLabel.text = "Durée"
        tvDurationValue.text = spell.duration

        tvContent.setHtmlText(spell.content)
    }
}
