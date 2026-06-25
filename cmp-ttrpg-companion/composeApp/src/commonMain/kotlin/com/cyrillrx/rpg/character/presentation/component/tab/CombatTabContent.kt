package com.cyrillrx.rpg.character.presentation.component.tab

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.component.section.CombatRow
import com.cyrillrx.rpg.character.presentation.component.section.SheetDivider
import com.cyrillrx.rpg.character.presentation.component.section.WalkSpeedRow
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
        maxHitPoints = state.character.maxHitPoints,
        onArmorClassTapped = { onFieldTapped(EditingField.ArmorClass) },
        onMaxHitPointsTapped = { onFieldTapped(EditingField.MaxHitPoints) },
    )

    WalkSpeedRow(
        walkSpeed = state.character.speeds.walk,
        onTap = { onFieldTapped(EditingField.WalkSpeed) },
    )
}
