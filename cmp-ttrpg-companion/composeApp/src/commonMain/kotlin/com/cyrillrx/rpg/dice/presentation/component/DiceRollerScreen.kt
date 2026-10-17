package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.SimpleTopBar
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.contentMaxWidth
import com.cyrillrx.rpg.core.presentation.theme.emptyStateIconAlpha
import com.cyrillrx.rpg.core.presentation.theme.emptyStateIconSize
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.core.presentation.theme.widthExpandedMin
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.navigation.DiceRouter
import com.cyrillrx.rpg.dice.presentation.viewmodel.DiceRollerViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_advantage
import rpg_companion.composeapp.generated.resources.btn_decrease_modifier
import rpg_companion.composeapp.generated.resources.btn_disadvantage
import rpg_companion.composeapp.generated.resources.btn_increase_modifier
import rpg_companion.composeapp.generated.resources.btn_remove_double_roll
import rpg_companion.composeapp.generated.resources.btn_remove_roll
import rpg_companion.composeapp.generated.resources.btn_reset_roll
import rpg_companion.composeapp.generated.resources.die_count
import rpg_companion.composeapp.generated.resources.die_label
import rpg_companion.composeapp.generated.resources.label_modifier
import rpg_companion.composeapp.generated.resources.message_tap_a_die
import rpg_companion.composeapp.generated.resources.title_dice_roller

private val dieTileMinHeight = 64.dp
private val rollTileMinSize = 40.dp
private val dieLabelWidth = 48.dp

@Composable
fun DiceRollerScreen(viewModel: DiceRollerViewModel, router: DiceRouter) {
    val pool by viewModel.pool.collectAsStateWithLifecycle()

    DiceRollerScreen(
        pool = pool,
        onNavigateUpClicked = router::navigateUp,
        onDieClicked = viewModel::roll,
        onAdvantageClicked = viewModel::rollD20WithAdvantage,
        onDisadvantageClicked = viewModel::rollD20WithDisadvantage,
        onRollClicked = viewModel::remove,
        onModifierDecreased = viewModel::decreaseModifier,
        onModifierIncreased = viewModel::increaseModifier,
        onResetClicked = viewModel::reset,
    )
}

@Composable
fun DiceRollerScreen(
    pool: RollPool,
    onNavigateUpClicked: () -> Unit,
    onDieClicked: (Die) -> Unit,
    onAdvantageClicked: () -> Unit,
    onDisadvantageClicked: () -> Unit,
    onRollClicked: (Int) -> Unit,
    onModifierDecreased: () -> Unit,
    onModifierIncreased: () -> Unit,
    onResetClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                titleResource = Res.string.title_dice_roller,
                onNavigateUpClicked = onNavigateUpClicked,
                actions = {
                    IconButton(onClick = onResetClicked, enabled = !pool.isEmpty() || pool.modifier != 0) {
                        Icon(Icons.Filled.RestartAlt, contentDescription = stringResource(Res.string.btn_reset_roll))
                    }
                },
            )
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
            val controls: @Composable (Modifier, Shape) -> Unit = { modifier, shape ->
                ControlPanel(
                    pool = pool,
                    onDieClicked = onDieClicked,
                    onAdvantageClicked = onAdvantageClicked,
                    onDisadvantageClicked = onDisadvantageClicked,
                    onModifierDecreased = onModifierDecreased,
                    onModifierIncreased = onModifierIncreased,
                    shape = shape,
                    modifier = modifier,
                )
            }

            if (maxWidth >= widthExpandedMin) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingCommon),
                    modifier = contentModifier.padding(spacingCommon),
                ) {
                    RollResultCard(pool, onRollClicked, modifier = Modifier.weight(1f).fillMaxHeight())
                    controls(Modifier.weight(1f), MaterialTheme.shapes.large)
                }
            } else {
                Column(modifier = contentModifier) {
                    RollResultCard(pool, onRollClicked, modifier = Modifier.weight(1f).padding(spacingCommon))
                    controls(
                        Modifier.fillMaxWidth(),
                        MaterialTheme.shapes.large.copy(bottomStart = CornerSize(0), bottomEnd = CornerSize(0)),
                    )
                }
            }
        }
    }
}

@Composable
private fun RollResultCard(pool: RollPool, onRollClicked: (Int) -> Unit, modifier: Modifier = Modifier) {
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
            Text(
                text = pool.toExpression()?.toNotation().orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            RollTotal(pool.total())
            if (pool.isEmpty()) EmptyRoll() else RollGroups(pool, onRollClicked)
        }
    }
}

@Composable
private fun RollTotal(total: Int) {
    AnimatedContent(
        targetState = total,
        transitionSpec = {
            (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
        },
    ) { value ->
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun EmptyRoll() {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.Casino,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = emptyStateIconAlpha),
            modifier = Modifier.size(emptyStateIconSize),
        )
        Text(text = stringResource(Res.string.message_tap_a_die), style = MaterialTheme.typography.bodyLarge)
    }
}

/** One line per die, from the biggest as the notation reads; each roll keeps its index in the pool. */
@Composable
private fun RollGroups(pool: RollPool, onRollClicked: (Int) -> Unit) {
    val rollsByDie = pool.rolls.withIndex().groupBy { it.value.die }.entries.sortedByDescending { it.key.sides }

    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium), modifier = Modifier.fillMaxWidth()) {
        rollsByDie.forEach { (die, rolls) ->
            RollGroup(die, rolls, onRollClicked)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RollGroup(die: Die, rolls: List<IndexedValue<DieRoll>>, onRollClicked: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = stringResource(Res.string.die_label, die.sides),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .width(dieLabelWidth)
                .padding(top = spacingMedium),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {
            rolls.forEach { (index, roll) -> RollTile(roll, onClick = { onRollClicked(index) }) }
        }
    }
}

@Composable
private fun RollTile(roll: DieRoll, onClick: () -> Unit) {
    val die = stringResource(Res.string.die_label, roll.die.sides)
    val description = when (roll) {
        is SimpleRoll -> stringResource(Res.string.btn_remove_roll, die, roll.value)
        is DoubleRoll -> stringResource(Res.string.btn_remove_double_roll, die, roll.value, roll.discarded.value)
    }
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier
            .sizeIn(minWidth = rollTileMinSize, minHeight = rollTileMinSize)
            .semantics { contentDescription = description },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
    Row(horizontalArrangement = Arrangement.spacedBy(spacingSmall)) {
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
    pool: RollPool,
    onDieClicked: (Die) -> Unit,
    onAdvantageClicked: () -> Unit,
    onDisadvantageClicked: () -> Unit,
    onModifierDecreased: () -> Unit,
    onModifierIncreased: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow, shape = shape, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingCommon),
            modifier = Modifier.padding(spacingCommon),
        ) {
            ModifierStepper(pool.modifier, onModifierDecreased, onModifierIncreased)
            DicePad(pool, onDieClicked, onAdvantageClicked, onDisadvantageClicked)
        }
    }
}

@Composable
private fun ModifierStepper(modifier: Int, onDecreased: () -> Unit, onIncreased: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(Res.string.label_modifier), style = MaterialTheme.typography.titleSmall)
        Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecreased, enabled = modifier > -MAX_MODIFIER) {
                    Icon(Icons.Filled.Remove, contentDescription = stringResource(Res.string.btn_decrease_modifier))
                }
                Text(text = modifier.toSignedString(), style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onIncreased, enabled = modifier < MAX_MODIFIER) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(Res.string.btn_increase_modifier))
                }
            }
        }
    }
}

/** A fixed grid, so each die stays under the same spot whatever the width; the d20 sits last, framed by its variants. */
@Composable
private fun DicePad(
    pool: RollPool,
    onDieClicked: (Die) -> Unit,
    onAdvantageClicked: () -> Unit,
    onDisadvantageClicked: () -> Unit,
) {
    val countByDie = pool.rolls.groupingBy { it.die }.eachCount()
    val enabled = !pool.isFull()
    val d20 = stringResource(Res.string.die_label, Die.D20.sides)

    Column(verticalArrangement = Arrangement.spacedBy(spacingMedium)) {
        listOf(listOf(Die.D4, Die.D6, Die.D8), listOf(Die.D10, Die.D12, Die.D100)).forEach { dice ->
            Row(horizontalArrangement = Arrangement.spacedBy(spacingMedium)) {
                dice.forEach { die ->
                    DieTile(
                        label = stringResource(Res.string.die_label, die.sides),
                        caption = countCaption(countByDie[die] ?: 0),
                        onClick = { onDieClicked(die) },
                        enabled = enabled,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacingMedium)) {
            DieTile(
                label = d20,
                caption = stringResource(Res.string.btn_disadvantage),
                onClick = onDisadvantageClicked,
                enabled = enabled,
                accent = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f),
            )
            DieTile(
                label = d20,
                caption = countCaption(countByDie[Die.D20] ?: 0),
                onClick = { onDieClicked(Die.D20) },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )
            DieTile(
                label = d20,
                caption = stringResource(Res.string.btn_advantage),
                onClick = onAdvantageClicked,
                enabled = enabled,
                accent = LocalHealthColors.current.heal,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** The [caption] line is always laid out, so a count appearing under a die does not shift the pad. */
@Composable
private fun DieTile(
    label: String,
    caption: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    accent: Color? = null,
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(borderWidth, (accent ?: MaterialTheme.colorScheme.outline).copy(alpha = borderAlpha)),
        colors = dieTileColors(accent),
        // A hovered card gains a shadow, which shows through the transparent advantage tiles.
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = modifier.heightIn(min = dieTileMinHeight),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dieTileMinHeight)
                .padding(spacingSmall),
        ) {
            Text(text = label, style = MaterialTheme.typography.titleLarge)
            Text(
                text = caption,
                style = MaterialTheme.typography.labelMedium,
                color = if (accent == null) MaterialTheme.colorScheme.tertiary else Color.Unspecified,
            )
        }
    }
}

/** Matches the healing and damage buttons of the character sheet: outlined, the accent on the text and a faint border. */
@Composable
private fun dieTileColors(accent: Color?): CardColors = if (accent == null) {
    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
} else {
    CardDefaults.cardColors(containerColor = Color.Transparent, contentColor = accent)
}

@Composable
private fun countCaption(count: Int): String = if (count > 0) stringResource(Res.string.die_count, count) else ""

private val previewPool = RollPool(
    rolls = listOf(
        DoubleRoll(kept = SimpleRoll(Die.D20, 17), discarded = SimpleRoll(Die.D20, 3)),
        SimpleRoll(Die.D6, 3),
        SimpleRoll(Die.D6, 5),
        SimpleRoll(Die.D6, 1),
        DoubleRoll(kept = SimpleRoll(Die.D20, 4), discarded = SimpleRoll(Die.D20, 12)),
    ),
    modifier = 2,
)

@Composable
private fun DiceRollerScreenPreview(pool: RollPool) {
    DiceRollerScreen(
        pool = pool,
        onNavigateUpClicked = {},
        onDieClicked = {},
        onAdvantageClicked = {},
        onDisadvantageClicked = {},
        onRollClicked = {},
        onModifierDecreased = {},
        onModifierIncreased = {},
        onResetClicked = {},
    )
}

@Preview
@Composable
private fun PreviewDiceRollerScreenLight() {
    AppThemePreview(darkTheme = false) { DiceRollerScreenPreview(previewPool) }
}

@Preview
@Composable
private fun PreviewDiceRollerScreenDark() {
    AppThemePreview(darkTheme = true) { DiceRollerScreenPreview(previewPool) }
}

@Preview
@Composable
private fun PreviewDiceRollerScreenEmpty() {
    AppThemePreview(darkTheme = true) { DiceRollerScreenPreview(RollPool()) }
}
