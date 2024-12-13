package com.cyrillrx.rpg.bestiary.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.componenent.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.bestiary.domain.Creature

@Composable
fun CreatureItem(creature: Creature) {
    Column(
        modifier = Modifier
            .padding(spacingCommon),
    ) {
        Text(
            text = creature.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        val abilities = creature.abilities
        AbilitiesLayout(
            str = abilities.str.getValueWithModifier(),
            dex = abilities.dex.getValueWithModifier(),
            con = abilities.con.getValueWithModifier(),
            int = abilities.int.getValueWithModifier(),
            wis = abilities.wis.getValueWithModifier(),
            cha = abilities.cha.getValueWithModifier(),
        )

        HtmlText(text = creature.description)
    }
}
