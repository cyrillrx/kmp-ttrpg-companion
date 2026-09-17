package com.cyrillrx.rpg.character.presentation.component.tab

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.hitPoints
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.component.section.CombatRow
import com.cyrillrx.rpg.character.presentation.component.section.HealthRow
import com.cyrillrx.rpg.character.presentation.component.section.SheetDivider
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_combat

@Composable
internal fun CombatTabContent(
    state: CharacterEditState.Loaded,
    onFieldTapped: (EditingField) -> Unit,
) {
    SheetDivider(stringResource(Res.string.label_combat))

    CombatRow(
        armorClass = state.character.armorClass,
        initiative = state.character.initiativeModifier(),
        walkSpeed = state.character.speeds.walk,
        onArmorClassTapped = { onFieldTapped(EditingField.ArmorClass) },
        onWalkSpeedTapped = { onFieldTapped(EditingField.WalkSpeed) },
    )

    HealthRow(
        hitPoints = state.character.hitPoints,
        onGaugeTapped = { onFieldTapped(EditingField.Health(HitPointAdjustment.MAXIMUM)) },
        onDamageTapped = { onFieldTapped(EditingField.Health(HitPointAdjustment.DAMAGE)) },
        onHealTapped = { onFieldTapped(EditingField.Health(HitPointAdjustment.HEALING)) },
    )
}
