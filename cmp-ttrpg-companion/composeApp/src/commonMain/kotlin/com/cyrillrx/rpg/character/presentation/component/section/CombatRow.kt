package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.presentation.component.StatCell
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.LocalDistanceUnit
import com.cyrillrx.rpg.core.presentation.format.toDistanceValue
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_ac
import rpg_companion.composeapp.generated.resources.label_initiative
import rpg_companion.composeapp.generated.resources.label_walk_speed
import rpg_companion.composeapp.generated.resources.settings_unit_feet_abbr
import rpg_companion.composeapp.generated.resources.settings_unit_meters_abbr

@Composable
internal fun CombatRow(
    armorClass: Int,
    initiative: Int,
    walkSpeed: Int,
    onArmorClassTapped: () -> Unit,
    onWalkSpeedTapped: () -> Unit,
) {
    val unit = LocalDistanceUnit.current
    val unitAbbrRes = when (unit) {
        DistanceUnit.FEET -> Res.string.settings_unit_feet_abbr
        DistanceUnit.METERS -> Res.string.settings_unit_meters_abbr
    }
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
            label = stringResource(Res.string.label_walk_speed, stringResource(unitAbbrRes)),
            value = walkSpeed.toDistanceValue(unit),
            onClick = onWalkSpeedTapped,
            modifier = Modifier.weight(1f),
        )
    }
}
