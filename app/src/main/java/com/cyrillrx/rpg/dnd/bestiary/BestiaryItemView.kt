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
import kotlinx.android.synthetic.main.item_bestiary.view.*

/**
 * @author Cyril Leroux
 *         Created on 12/03/2020.
 */
class BestiaryItemView(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.item_bestiary)) {

    var tvTitle: TextView = itemView.tvTitle
    var tvDescription: TextView = itemView.tvDescription
    var abilitiesView: AbilitiesView = itemView.abilitiesLayout

    fun bind(bestiaryItem: BestiaryItem) {

        val creature = bestiaryItem.toCreature()

        tvTitle.text = creature.name
        tvDescription.setHtmlText(creature.description)
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