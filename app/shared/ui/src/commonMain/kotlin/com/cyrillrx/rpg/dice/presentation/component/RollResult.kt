package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.core.presentation.theme.dieLabelWidth
import com.cyrillrx.rpg.core.presentation.theme.rollTileMinSize
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.die_label
import rpg_companion.app.shared.ui.generated.resources.label_double_roll

/** [rollCount] tells two identical rolls apart, so the total animates on each new roll. */
@Composable
internal fun RollResult(roll: RollPool, rollCount: Int, modifier: Modifier = Modifier, showNotation: Boolean = true) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingCommon),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (showNotation) {
            Text(
                text = roll.toNotation().orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        RollTotal(rollCount, roll.total())
        RollGroups(roll)
    }
}

// TODO(#353): give the private composables below a modifier parameter
@Composable
private fun RollTotal(rollCount: Int, total: Int) {
    AnimatedContent(
        targetState = rollCount to total,
        transitionSpec = {
            (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
        },
    ) { (_, value) ->
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun RollGroups(roll: RollPool) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium), modifier = Modifier.fillMaxWidth()) {
        roll.rollsByDie().forEach { (die, rolls) -> RollGroup(die, rolls) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RollGroup(die: Die, rolls: List<DieRoll>) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = stringResource(Res.string.die_label, die.sides),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .widthIn(min = dieLabelWidth)
                .padding(top = spacingMedium),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {
            rolls.forEach { roll -> RollTile(roll) }
        }
    }
}

@Composable
private fun RollTile(roll: DieRoll) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.sizeIn(minWidth = rollTileMinSize, minHeight = rollTileMinSize),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingSmall),
        ) {
            when (roll) {
                is SimpleRoll -> Text(text = roll.value.toString(), style = MaterialTheme.typography.titleMedium)
                is DoubleRoll -> DoubleRollContent(
                    roll,
                    keptStyle = MaterialTheme.typography.titleMedium,
                    discardedStyle = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

// TODO(#335): say the keep rule in the description, so that a tie tells the mode apart

/** The discarded roll is struck through, so it reads as discarded without relying on color. */
@Composable
internal fun DoubleRollContent(
    roll: DoubleRoll,
    keptStyle: TextStyle,
    discardedStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(
        Res.string.label_double_roll,
        stringResource(Res.string.die_label, roll.die.sides),
        roll.kept.value,
        roll.discarded.value,
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        Text(
            text = roll.kept.value.toString(),
            style = keptStyle,
            color = rollColor(roll.kept, against = roll.discarded),
            modifier = Modifier.alignByBaseline(),
        )
        Text(
            text = roll.discarded.value.toString(),
            style = discardedStyle,
            color = rollColor(roll.discarded, against = roll.kept),
            textDecoration = TextDecoration.LineThrough,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

// TODO(#333): color by DoubleRoll.rule, so that a tie still tells the mode apart

/** The higher roll reads like healing and the lower like damage, so the kept one tells advantage from disadvantage. */
@Composable
private fun rollColor(roll: SimpleRoll, against: SimpleRoll): Color = when {
    roll.value > against.value -> LocalHealthColors.current.heal
    roll.value < against.value -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurface
}

private val previewRoll = RollPool(
    rolls = listOf(
        DoubleRoll(
            kept = SimpleRoll(Die.D20, value = 17),
            discarded = SimpleRoll(Die.D20, value = 3),
            rule = KeepRule.HIGHER,
        ),
        SimpleRoll(Die.D6, value = 4),
        SimpleRoll(Die.D6, value = 2),
    ),
    modifier = 5,
)

@Preview
@Composable
private fun PreviewRollResultLight() {
    AppThemePreview(darkTheme = false) { RollResult(previewRoll, rollCount = 1) }
}

@Preview
@Composable
private fun PreviewRollResultDark() {
    AppThemePreview(darkTheme = true) { RollResult(previewRoll, rollCount = 1) }
}
