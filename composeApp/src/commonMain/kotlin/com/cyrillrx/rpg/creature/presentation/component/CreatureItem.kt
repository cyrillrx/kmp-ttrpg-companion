package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.component.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.creature.domain.Creature

@Composable
fun CreatureItem(
    creature: Creature,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(spacingCommon),
    ) {
        Text(
            text = creature.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        MainStatLayout(
            creature = creature,
            modifier = Modifier.fillMaxWidth(),
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
