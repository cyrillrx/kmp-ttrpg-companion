package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.toPlainNotation
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun RollLine(
    pool: RollPool,
    notationStyle: TextStyle,
    totalStyle: TextStyle,
    totalColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            itemVerticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = pool.toPlainNotation().orEmpty(),
                style = notationStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            pool.keepRule()?.let { rule -> KeepRuleShortLabel(rule, style = notationStyle) }
        }

        Text(text = pool.total().toString(), style = totalStyle, color = totalColor)
    }
}

@Composable
private fun RollLinePreview() {
    val simple = RollPool(listOf(SimpleRoll(Die.D8, value = 6), SimpleRoll(Die.D6, value = 4)), modifier = 2)
    val disadvantage = RollPool(
        listOf(
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 4),
                discarded = SimpleRoll(Die.D20, value = 15),
                rule = KeepRule.LOWER,
            ),
        ),
        modifier = 5,
    )

    Column {
        listOf(simple, disadvantage).forEach { pool ->
            RollLine(
                pool,
                notationStyle = MaterialTheme.typography.bodyMedium,
                totalStyle = MaterialTheme.typography.titleLarge,
                totalColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRollLineLight() {
    AppThemePreview(darkTheme = false) { RollLinePreview() }
}

@Preview
@Composable
private fun PreviewRollLineDark() {
    AppThemePreview(darkTheme = true) { RollLinePreview() }
}
