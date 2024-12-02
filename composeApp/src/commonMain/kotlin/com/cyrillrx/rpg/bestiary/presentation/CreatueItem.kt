package com.cyrillrx.rpg.bestiary.presentation;

import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.models.bestiary.Creature

@Composable
fun CreatureItem(modifier: Modifier, creature: Creature) {
    val abilities = creature.abilities
    AbilitiesLayout(
        strValue = abilities.str,
        dexValue = abilities.dex,
        conValue = abilities.con,
        intValue = abilities.int,
        wisValue = abilities.wis,
        chaValue = abilities.cha,
    )
}
