package com.cyrillrx.rpg.character.presentation.component.dialog

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
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toNameString
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
import com.cyrillrx.rpg.dice.presentation.component.RollResult
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_initiative
import rpg_companion.composeapp.generated.resources.label_saving_throw_of

/** What the sheet can ask for about a d20 check, grouped so the screen does not thread five callbacks. */
class D20CheckActions(
    val onCheckTapped: (D20Check) -> Unit,
    val onRerollClicked: () -> Unit,
    val onAdvantageClicked: () -> Unit,
    val onDisadvantageClicked: () -> Unit,
    val onDismissed: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun D20CheckSheet(sheetRoll: SheetRoll, actions: D20CheckActions) {
    ModalBottomSheet(
        onDismissRequest = actions.onDismissed,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier
                .padding(horizontal = spacingCommon)
                .padding(bottom = spacingLarge),
        ) {
            Text(text = sheetRoll.check.toLabel(), style = MaterialTheme.typography.titleLarge)
            RollResult(sheetRoll.lastRoll, sheetRoll.rollCount)
            D20Row(
                onDisadvantageClicked = actions.onDisadvantageClicked,
                onD20Clicked = actions.onRerollClicked,
                onAdvantageClicked = actions.onAdvantageClicked,
            )
        }
    }
}

@Composable
private fun D20Check.toLabel(): String = when (this) {
    is D20Check.SavingThrow -> stringResource(Res.string.label_saving_throw_of, ability.toNameString())
    is D20Check.SkillCheck -> skill.toFormattedString()
    D20Check.Initiative -> stringResource(Res.string.label_initiative)
}

@Preview
@Composable
private fun PreviewD20CheckSheetLight() {
    AppThemePreview(darkTheme = false) { D20CheckSheet(previewRoll, previewActions) }
}

@Preview
@Composable
private fun PreviewD20CheckSheetDark() {
    AppThemePreview(darkTheme = true) { D20CheckSheet(previewRoll, previewActions) }
}

private val previewRoll = SheetRoll(
    check = D20Check.SkillCheck(Skill.PERCEPTION),
    lastRoll = RollPool(
        rolls = listOf(DoubleRoll(SimpleRoll(Die.D20, 17), SimpleRoll(Die.D20, 4), KeepRule.HIGHER)),
        modifier = 5,
    ),
    rollCount = 1,
)

private val previewActions = D20CheckActions({}, {}, {}, {}, {})
