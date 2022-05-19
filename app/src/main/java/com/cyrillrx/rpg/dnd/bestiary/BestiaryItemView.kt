package com.cyrillrx.rpg.dnd.bestiary

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyrillrx.android.utils.inflate
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.bestiary.BestiaryItem
import com.cyrillrx.rpg.layout.AbilitiesView
import com.cyrillrx.rpg.models.bestiary.Abilities
import com.cyrillrx.rpg.models.bestiary.Creature
import com.cyrillrx.rpg.setHtmlText

class BestiaryItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_bestiary)) {

    private var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private var tvContent: TextView = itemView.findViewById(R.id.tvContent)
    private var abilitiesView: AbilitiesView = itemView.findViewById(R.id.abilitiesLayout)

    fun bind(bestiaryItem: BestiaryItem) {

        val creature = bestiaryItem.toCreature()

        tvTitle.text = creature.name
        tvContent.setHtmlText(creature.description)
        abilitiesView.setAbilities(creature.abilities)
    }

    private fun BestiaryItem.toCreature(): Creature {

        val abilities = header?.monster

        return Creature(
            title ?: "",
            content ?: "",
            type ?: "",
            subtype ?: "",
            size ?: "",
            alignment ?: "",
            Abilities(
                extractAbilities(abilities?.str),
                extractAbilities(abilities?.dex),
                extractAbilities(abilities?.con),
                extractAbilities(abilities?.int),
                extractAbilities(abilities?.wis),
                extractAbilities(abilities?.cha)
            )
        )
    }

    private fun extractAbilities(ability: String?): Int {
        return ability
            ?.split(" ")
            ?.firstOrNull()
            ?.toIntOrNull()
            ?: Abilities.DEFAULT_ABILITY_VALUE
    }
}
