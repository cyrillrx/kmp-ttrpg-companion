package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.character.presentation.SheetRoll
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingLarge
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.component.D20Row
import com.cyrillrx.rpg.dice.presentation.component.KeepRuleLabel
import com.cyrillrx.rpg.dice.presentation.component.RollResult
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.label_saving_throw
import rpg_companion.app.shared.ui.generated.resources.value_check_modifier

class D20CheckActions(
    val onRerollClicked: () -> Unit,
    val onAdvantageClicked: () -> Unit,
    val onDisadvantageClicked: () -> Unit,
    val onDismissed: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun D20CheckBottomSheet(sheetRoll: SheetRoll, actions: D20CheckActions, modifier: Modifier = Modifier) {
    ModalBottomSheet(
        onDismissRequest = actions.onDismissed,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier
                .padding(horizontal = spacingCommon)
                .padding(bottom = spacingLarge),
        ) {
            CheckHeader(sheetRoll)
            RollResult(sheetRoll.lastRoll, sheetRoll.rollCount, showNotation = false)
            D20Row(
                onDisadvantageClicked = actions.onDisadvantageClicked,
                onD20Clicked = actions.onRerollClicked,
                onAdvantageClicked = actions.onAdvantageClicked,
            )
        }
    }
}

@Composable
private fun CheckHeader(sheetRoll: SheetRoll, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        val check = sheetRoll.check
        if (check is D20Check.SavingThrow) {
            Text(
                text = stringResource(Res.string.label_saving_throw),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Text(
            text = stringResource(
                Res.string.value_check_modifier,
                check.toFormattedString(),
                sheetRoll.lastRoll.modifier.toSignedString(),
            ),
            style = MaterialTheme.typography.titleLarge,
        )
        sheetRoll.lastRoll.keepRule()?.let { rule ->
            KeepRuleLabel(rule, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Preview
@Composable
private fun PreviewD20CheckBottomSheetLight() {
    AppThemePreview(darkTheme = false) { D20CheckBottomSheet(previewRoll, previewActions) }
}

@Preview
@Composable
private fun PreviewD20CheckBottomSheetDark() {
    AppThemePreview(darkTheme = true) { D20CheckBottomSheet(previewRoll, previewActions) }
}

private val previewRoll = SheetRoll(
    check = D20Check.SkillCheck(Skill.PERCEPTION),
    lastRoll = RollPool(
        rolls = listOf(DoubleRoll(SimpleRoll(Die.D20, value = 17), SimpleRoll(Die.D20, value = 4), KeepRule.HIGHER)),
        modifier = 5,
    ),
    rollCount = 1,
)

private val previewActions = D20CheckActions({}, {}, {}, {})
