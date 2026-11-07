package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.contentMaxWidth
import com.cyrillrx.rpg.core.presentation.theme.controlPanelMaxHeightFraction
import com.cyrillrx.rpg.core.presentation.theme.emptyStateIconAlpha
import com.cyrillrx.rpg.core.presentation.theme.emptyStateIconSize
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.modifierValueMinWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.core.presentation.theme.widthExpandedMin
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.MAX_DICE_COUNT
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.DiceRollerState
import com.cyrillrx.rpg.dice.presentation.navigation.DiceRouter
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceHistoryViewModel
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceRollerViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.btn_clear
import rpg_companion.app.shared.ui.generated.resources.btn_decrease_modifier
import rpg_companion.app.shared.ui.generated.resources.btn_increase_modifier
import rpg_companion.app.shared.ui.generated.resources.btn_roll
import rpg_companion.app.shared.ui.generated.resources.btn_roll_composition
import rpg_companion.app.shared.ui.generated.resources.btn_roll_history
import rpg_companion.app.shared.ui.generated.resources.die_count
import rpg_companion.app.shared.ui.generated.resources.die_label
import rpg_companion.app.shared.ui.generated.resources.label_modifier
import rpg_companion.app.shared.ui.generated.resources.label_previous_rolls
import rpg_companion.app.shared.ui.generated.resources.message_compose_then_roll
import rpg_companion.app.shared.ui.generated.resources.section_compose
import rpg_companion.app.shared.ui.generated.resources.section_d20_roll
import rpg_companion.app.shared.ui.generated.resources.title_dice_roller
import kotlin.time.Instant

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

class DiceHistoryActions(val onOpenClicked: () -> Unit, val onClearClicked: () -> Unit)

@Composable
fun DiceRollerScreen(viewModel: DiceRollerViewModel, historyViewModel: DiceHistoryViewModel, router: DiceRouter) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val history by historyViewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val onClearHistoryClicked = rememberRollHistoryClear(historyViewModel, snackbarHostState)
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

    val historyActions = remember(router, onClearHistoryClicked) {
        DiceHistoryActions(onOpenClicked = router::openHistory, onClearClicked = onClearHistoryClicked)
    }

    DiceRollerScreen(
        state = state,
        history = history,
        snackbarHostState = snackbarHostState,
        onNavigateUpClicked = router::navigateUp,
        actions = actions,
        historyActions = historyActions,
    )
}

@Composable
fun DiceRollerScreen(
    state: DiceRollerState,
    history: DiceHistoryState,
    snackbarHostState: SnackbarHostState,
    onNavigateUpClicked: () -> Unit,
    actions: DiceRollerActions,
    historyActions: DiceHistoryActions,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(titleResource = Res.string.title_dice_roller, onNavigateUpClicked = onNavigateUpClicked)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

            if (maxWidth >= widthExpandedMin) {
                ExpandedLayout(state, history, actions, historyActions.onClearClicked, contentModifier)
            } else {
                CompactLayout(
                    state = state,
                    history = history,
                    actions = actions,
                    onHistoryClicked = historyActions.onOpenClicked,
                    controlPanelMaxHeight = maxHeight * controlPanelMaxHeightFraction,
                    modifier = contentModifier,
                )
            }
        }
    }
}

// TODO(#367): reconcile the roller and history states in one screen state, tested outside the composables
@Composable
private fun ExpandedLayout(
    state: DiceRollerState,
    history: DiceHistoryState,
    actions: DiceRollerActions,
    onClearHistoryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(spacingCommon), modifier = modifier.padding(spacingCommon)) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingCommon), modifier = Modifier.weight(1f)) {
            RollResultCard(
                state = state.hidingRollsUpTo(history.hiddenUpTo),
                onHistoryClicked = null,
                modifier = Modifier.weight(1f),
            )
            DiceHistorySection(
                state = history.without(state.lastRollId),
                onClearClicked = onClearHistoryClicked,
                modifier = Modifier.weight(1f),
            )
        }

        ControlPanel(state, actions, MaterialTheme.shapes.large, Modifier.weight(1f))
    }
}

@Composable
private fun CompactLayout(
    state: DiceRollerState,
    history: DiceHistoryState,
    actions: DiceRollerActions,
    onHistoryClicked: () -> Unit,
    controlPanelMaxHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        RollResultCard(
            state = state.hidingRollsUpTo(history.hiddenUpTo),
            onHistoryClicked = onHistoryClicked,
            modifier = Modifier.weight(1f).padding(spacingCommon),
        )
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

@Composable
private fun RollResultCard(state: DiceRollerState, onHistoryClicked: (() -> Unit)?, modifier: Modifier = Modifier) {
    val lastRoll = state.lastRoll

    Card(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(spacingCommon),
        ) {
            if (lastRoll == null) {
                NoRollYet()
            } else {
                RollResult(lastRoll, state.rollCount)
            }
        }

        if (onHistoryClicked != null) {
            HistoryShortcuts(state, onHistoryClicked)
        }
    }
}

@Composable
private fun ColumnScope.HistoryShortcuts(state: DiceRollerState, onHistoryClicked: () -> Unit) {
    val previousRolls = state.previousRolls()
    if (previousRolls.isNotEmpty()) {
        PreviousRolls(previousRolls, modifier = Modifier.padding(horizontal = spacingCommon))
    }

    TextButton(
        onClick = onHistoryClicked,
        modifier = Modifier
            .align(Alignment.End)
            .padding(end = spacingMedium, bottom = spacingSmall),
    ) {
        Text(text = stringResource(Res.string.btn_roll_history))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(iconSizeMedium),
        )
    }
}

@Composable
private fun PreviousRolls(rolls: List<RecordedRoll>, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingSmall), modifier = modifier.fillMaxWidth()) {
        HorizontalDivider()
        Text(
            text = stringResource(Res.string.label_previous_rolls),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = spacingSmall),
        )
        rolls.forEach { recorded ->
            RollLine(
                recorded.pool,
                notationStyle = MaterialTheme.typography.bodySmall,
                totalStyle = MaterialTheme.typography.bodySmall,
                totalColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
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
            D20Row(
                onDisadvantageClicked = actions.onDisadvantageClicked,
                onD20Clicked = actions.onD20Clicked,
                onAdvantageClicked = actions.onAdvantageClicked,
            )
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

private val previewState = DiceRollerState(
    countByDie = mapOf(Die.D6 to 2),
    modifier = 5,
    lastRoll = RollPool(
        rolls = listOf(
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 17),
                discarded = SimpleRoll(Die.D20, value = 3),
                rule = KeepRule.HIGHER,
            ),
        ),
        modifier = 5,
    ),
    recentRolls = listOf(
        RecordedRoll(
            id = 2,
            pool = RollPool(listOf(SimpleRoll(Die.D8, value = 6), SimpleRoll(Die.D6, value = 5)), modifier = 2),
            rolledAt = Instant.fromEpochMilliseconds(epochMilliseconds = 0),
        ),
        RecordedRoll(
            id = 1,
            pool = RollPool(listOf(SimpleRoll(Die.D20, value = 8))),
            rolledAt = Instant.fromEpochMilliseconds(epochMilliseconds = 0),
        ),
    ),
)

private val previewActions = DiceRollerActions({}, {}, {}, {}, {}, {}, {}, {}, {})

private val previewHistoryActions = DiceHistoryActions(onOpenClicked = {}, onClearClicked = {})

@Composable
private fun DiceRollerScreenPreview(state: DiceRollerState) {
    DiceRollerScreen(
        state = state,
        history = DiceHistoryState(previewHistory),
        snackbarHostState = remember { SnackbarHostState() },
        onNavigateUpClicked = {},
        actions = previewActions,
        historyActions = previewHistoryActions,
    )
}

@Preview
@Composable
private fun PreviewDiceRollerScreenLight() {
    AppThemePreview(darkTheme = false) { DiceRollerScreenPreview(previewState) }
}

@Preview
@Composable
private fun PreviewDiceRollerScreenDark() {
    AppThemePreview(darkTheme = true) { DiceRollerScreenPreview(previewState) }
}

@Preview
@Composable
private fun PreviewDiceRollerScreenEmpty() {
    AppThemePreview(darkTheme = true) { DiceRollerScreenPreview(DiceRollerState()) }
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun PreviewDiceRollerScreenWideLight() {
    AppThemePreview(darkTheme = false) { DiceRollerScreenPreview(previewState) }
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun PreviewDiceRollerScreenWideDark() {
    AppThemePreview(darkTheme = true) { DiceRollerScreenPreview(previewState) }
}
