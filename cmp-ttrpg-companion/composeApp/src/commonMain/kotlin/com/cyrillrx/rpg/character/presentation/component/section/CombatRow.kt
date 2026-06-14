package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_ac
import rpg_companion.composeapp.generated.resources.label_initiative
import rpg_companion.composeapp.generated.resources.label_max_hp

@Composable
internal fun CombatRow(
    armorClass: Int,
    initiative: Int,
    maxHitPoints: Int,
    onArmorClassTapped: () -> Unit,
    onMaxHitPointsTapped: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier.fillMaxWidth(),
    ) {
        StatCell(
            label = stringResource(Res.string.label_ac),
            value = armorClass.toString(),
            onClick = onArmorClassTapped,
            modifier = Modifier.weight(1f),
        )
        StatCell(
            label = stringResource(Res.string.label_initiative),
            value = initiative.toSignedString(),
            modifier = Modifier.weight(1f),
        )
        StatCell(
            label = stringResource(Res.string.label_max_hp),
            value = maxHitPoints.toString(),
            onClick = onMaxHitPointsTapped,
            modifier = Modifier.weight(1f),
        )
    }
}
