package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.contentMaxWidth
import com.cyrillrx.rpg.core.presentation.theme.controlPanelMaxHeightFraction
import com.cyrillrx.rpg.core.presentation.theme.dieLabelWidth
import com.cyrillrx.rpg.core.presentation.theme.dieTileMinHeight
import com.cyrillrx.rpg.core.presentation.theme.emptyStateIconAlpha
import com.cyrillrx.rpg.core.presentation.theme.emptyStateIconSize
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.iconSizeSmall
import com.cyrillrx.rpg.core.presentation.theme.modifierValueMinWidth
import com.cyrillrx.rpg.core.presentation.theme.removeDieButtonSize
import com.cyrillrx.rpg.core.presentation.theme.rollTileMinSize
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.core.presentation.theme.widthExpandedMin
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.DiceRollerState
import com.cyrillrx.rpg.dice.presentation.navigation.DiceRouter
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceRollerViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_advantage
import rpg_companion.composeapp.generated.resources.btn_clear
import rpg_companion.composeapp.generated.resources.btn_decrease_modifier
import rpg_companion.composeapp.generated.resources.btn_disadvantage
import rpg_companion.composeapp.generated.resources.btn_increase_modifier
import rpg_companion.composeapp.generated.resources.btn_remove_die_of
import rpg_companion.composeapp.generated.resources.btn_roll
import rpg_companion.composeapp.generated.resources.btn_roll_composition
import rpg_companion.composeapp.generated.resources.die_count
import rpg_companion.composeapp.generated.resources.die_label
import rpg_companion.composeapp.generated.resources.label_double_roll
import rpg_companion.composeapp.generated.resources.label_modifier
import rpg_companion.composeapp.generated.resources.message_compose_then_roll
import rpg_companion.composeapp.generated.resources.notation_with_advantage
import rpg_companion.composeapp.generated.resources.notation_with_disadvantage
import rpg_companion.composeapp.generated.resources.section_compose
import rpg_companion.composeapp.generated.resources.section_d20_roll
import rpg_companion.composeapp.generated.resources.title_dice_roller

/** What the roller screen can ask for, grouped so the panel does not thread ten callbacks through each level. */
class DiceRollerActions(
    val onDieAdded: (Die) -> Unit,
    val onDieRemoved: (Die) -> Unit,
    val onRollClicked: () -> Unit,
    val onD20Clicked: () -> Unit,
    val onAdvantageClicked: () -> Unit,
    val onDisadvantageClicked: () -> Unit,
    val onModifierDecreased: () -> Unit,
    val onModifierIncreased: () -> Unit,
    val onClearClicked: () -> Unit,
)

@Composable
fun DiceRollerScreen(viewModel: DiceRollerViewModel, router: DiceRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        DiceRollerActions(
            onDieAdded = viewModel::addDie,
            onDieRemoved = viewModel::removeDie,
            onRollClicked = viewModel::roll,
            onD20Clicked = viewModel::rollD20,
            onAdvantageClicked = viewModel::rollD20WithAdvantage,
            onDisadvantageClicked = viewModel::rollD20WithDisadvantage,
            onModifierDecreased = viewModel::decreaseModifier,
            onModifierIncreased = viewModel::increaseModifier,
            onClearClicked = viewModel::clearComposition,
        )
    }

    DiceRollerScreen(state = state, onNavigateUpClicked = router::navigateUp, actions = actions)
}

@Composable
fun DiceRollerScreen(state: DiceRollerState, onNavigateUpClicked: () -> Unit, actions: DiceRollerActions) {
    Scaffold(
        topBar = {
            SimpleTopBar(titleResource = Res.string.title_dice_roller, onNavigateUpClicked = onNavigateUpClicked)
        },
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            val contentModifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = contentMaxWidth)
                .fillMaxSize()
            val controlPanelMaxHeight = maxHeight * controlPanelMaxHeightFraction

            if (maxWidth >= widthExpandedMin) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingCommon),
                    modifier = contentModifier.padding(spacingCommon),
                ) {
                    RollResultCard(state, modifier = Modifier.weight(1f).fillMaxHeight())
                    ControlPanel(state, actions, MaterialTheme.shapes.large, Modifier.weight(1f))
                }
            } else {
                Column(modifier = contentModifier) {
                    RollResultCard(state, modifier = Modifier.weight(1f).padding(spacingCommon))
                    ControlPanel(
                        state = state,
                        actions = actions,
                        shape = MaterialTheme.shapes.large.copy(bottomStart = CornerSize(0), bottomEnd = CornerSize(0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = controlPanelMaxHeight),
                    )
                }
            }
        }
    }
}

@Composable
private fun RollResultCard(state: DiceRollerState, modifier: Modifier = Modifier) {
    val lastRoll = state.lastRoll

    Card(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingCommon),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(spacingCommon),
        ) {
            if (lastRoll == null) {
                NoRollYet()
            } else {
                Text(
                    text = lastRoll.notation(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                RollTotal(state.rollCount, lastRoll.total())
                RollGroups(lastRoll)
            }
        }
    }
}

@Composable
private fun RollPool.notation(): String {
    val doubleRollNotations = rolls.filterIsInstance<DoubleRoll>().associateWith { it.notation() }
    return toNotation(doubleRollNotations::getValue).orEmpty()
}

@Composable
private fun DoubleRoll.notation(): String = stringResource(
    when (rule) {
        KeepRule.HIGHER -> Res.string.notation_with_advantage
        KeepRule.LOWER -> Res.string.notation_with_disadvantage
    },
    stringResource(Res.string.die_label, die.sides),
)

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
private fun NoRollYet() {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = spacingCommon),
    ) {
        Icon(
            imageVector = Icons.Filled.Casino,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = emptyStateIconAlpha),
            modifier = Modifier.size(emptyStateIconSize),
        )
        Text(text = stringResource(Res.string.message_compose_then_roll), style = MaterialTheme.typography.bodyLarge)
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
                is DoubleRoll -> DoubleRollContent(roll)
            }
        }
    }
}

@Composable
private fun DoubleRollContent(roll: DoubleRoll) {
    val description = stringResource(
        Res.string.label_double_roll,
        stringResource(Res.string.die_label, roll.die.sides),
        roll.kept.value,
        roll.discarded.value,
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        Text(
            text = roll.kept.value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = rollColor(roll.kept, against = roll.discarded),
            modifier = Modifier.alignByBaseline(),
        )
        Text(
            text = roll.discarded.value.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = rollColor(roll.discarded, against = roll.kept),
            modifier = Modifier.alignByBaseline(),
        )
    }
}

/** The higher roll reads like healing and the lower like damage, so the kept one tells advantage from disadvantage. */
@Composable
private fun rollColor(roll: SimpleRoll, against: SimpleRoll): Color = when {
    roll.value > against.value -> LocalHealthColors.current.heal
    roll.value < against.value -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurface
}

@Composable
private fun ControlPanel(
    state: DiceRollerState,
    actions: DiceRollerActions,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow, shape = shape, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(spacingCommon),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(Res.string.label_modifier), style = MaterialTheme.typography.titleSmall)
                ModifierStepper(state.modifier, actions.onModifierDecreased, actions.onModifierIncreased)
            }
            SectionLabel(stringResource(Res.string.section_d20_roll))
            DirectD20Row(actions)
            SectionLabel(stringResource(Res.string.section_compose))
            CompositionPad(state, actions)
            RollButton(state, actions.onRollClicked)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = spacingSmall),
    )
}

@Composable
private fun ModifierStepper(modifier: Int, onDecreased: () -> Unit, onIncreased: () -> Unit) {
    Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecreased, enabled = modifier > -MAX_MODIFIER) {
                Icon(Icons.Filled.Remove, contentDescription = stringResource(Res.string.btn_decrease_modifier))
            }
            Text(
                text = modifier.toSignedString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = modifierValueMinWidth),
            )
            IconButton(onClick = onIncreased, enabled = modifier < MAX_MODIFIER) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(Res.string.btn_increase_modifier))
            }
        }
    }
}

@Composable
private fun DirectD20Row(actions: DiceRollerActions) {
    val d20 = stringResource(Res.string.die_label, Die.D20.sides)

    Row(horizontalArrangement = Arrangement.spacedBy(spacingMedium), modifier = Modifier.height(IntrinsicSize.Min)) {
        DieTile(
            label = d20,
            caption = stringResource(Res.string.btn_disadvantage),
            onClick = actions.onDisadvantageClicked,
            accent = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
        )
        DieTile(
            label = d20,
            caption = "",
            onClick = actions.onD20Clicked,
            modifier = Modifier.weight(1f),
        )
        DieTile(
            label = d20,
            caption = stringResource(Res.string.btn_advantage),
            onClick = actions.onAdvantageClicked,
            accent = LocalHealthColors.current.heal,
            modifier = Modifier.weight(1f),
        )
    }
}

/** A fixed grid, so each die stays under the same spot whatever the width. */
@Composable
private fun CompositionPad(state: DiceRollerState, actions: DiceRollerActions) {
    val canAdd = state.diceCount() < MAX_DICE_COUNT
    val rows = listOf(listOf(Die.D4, Die.D6, Die.D8, Die.D10), listOf(Die.D12, Die.D20, Die.D100))

    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium)) {
        rows.forEach { dice ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                dice.forEach { die ->
                    val count = state.countByDie[die] ?: 0
                    DieTile(
                        label = stringResource(Res.string.die_label, die.sides),
                        caption = if (count > 0) stringResource(Res.string.die_count, count) else "",
                        onClick = { actions.onDieAdded(die) },
                        enabled = canAdd,
                        onRemove = if (count > 0) ({ actions.onDieRemoved(die) }) else null,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (dice == rows.last()) {
                    DieTile(
                        label = stringResource(Res.string.btn_clear),
                        caption = stringResource(Res.string.btn_clear),
                        onClick = actions.onClearClicked,
                        enabled = state.countByDie.isNotEmpty(),
                        icon = Icons.Filled.RestartAlt,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun RollButton(state: DiceRollerState, onClick: () -> Unit) {
    val composition = state.composition()
    Button(onClick = onClick, enabled = composition != null, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Filled.Casino, contentDescription = null, modifier = Modifier.size(iconSizeMedium))
        Text(
            text = composition?.let { stringResource(Res.string.btn_roll_composition, it.toNotation()) }
                ?: stringResource(Res.string.btn_roll),
            modifier = Modifier.padding(start = spacingMedium),
        )
    }
}

/** The [caption] line is always laid out, so a count appearing under a die does not shift the pad. */
@Composable
private fun DieTile(
    label: String,
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Color? = null,
    onRemove: (() -> Unit)? = null,
    icon: ImageVector? = null,
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(borderWidth, (accent ?: MaterialTheme.colorScheme.outline).copy(alpha = borderAlpha)),
        colors = dieTileColors(accent),
        // A hovered card gains a shadow, which shows through the transparent advantage tiles.
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = dieTileMinHeight),
    ) {
        Box(modifier = Modifier.fillMaxSize().heightIn(min = dieTileMinHeight)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(spacingSmall),
            ) {
                if (icon == null) {
                    Text(text = label, style = MaterialTheme.typography.titleLarge)
                } else {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(iconSizeMedium))
                }
                Text(
                    text = caption,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (accent == null) MaterialTheme.colorScheme.tertiary else Color.Unspecified,
                )
            }
            onRemove?.let { remove ->
                IconButton(
                    onClick = remove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(removeDieButtonSize),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = stringResource(Res.string.btn_remove_die_of, label),
                        modifier = Modifier.size(iconSizeSmall),
                    )
                }
            }
        }
    }
}

@Composable
private fun dieTileColors(accent: Color?): CardColors = if (accent == null) {
    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
} else {
    CardDefaults.cardColors(containerColor = Color.Transparent, contentColor = accent)
}

private val previewState = DiceRollerState(
    countByDie = mapOf(Die.D6 to 2),
    modifier = 5,
    lastRoll = RollPool(
        rolls = listOf(DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3), KeepRule.HIGHER)),
        modifier = 5,
    ),
)

private val previewActions = DiceRollerActions({}, {}, {}, {}, {}, {}, {}, {}, {})

@Preview
@Composable
private fun PreviewDiceRollerScreenLight() {
    AppThemePreview(darkTheme = false) { DiceRollerScreen(previewState, onNavigateUpClicked = {}, previewActions) }
}

@Preview
@Composable
private fun PreviewDiceRollerScreenDark() {
    AppThemePreview(darkTheme = true) { DiceRollerScreen(previewState, onNavigateUpClicked = {}, previewActions) }
}

@Preview
@Composable
private fun PreviewDiceRollerScreenEmpty() {
    AppThemePreview(darkTheme = true) { DiceRollerScreen(DiceRollerState(), onNavigateUpClicked = {}, previewActions) }
}
