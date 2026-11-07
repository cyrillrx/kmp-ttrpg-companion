package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.dice.domain.KeepRule
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.btn_advantage
import rpg_companion.app.shared.ui.generated.resources.btn_disadvantage
import rpg_companion.app.shared.ui.generated.resources.label_with_advantage
import rpg_companion.app.shared.ui.generated.resources.label_with_disadvantage

@Composable
internal fun KeepRuleLabel(rule: KeepRule, style: TextStyle, modifier: Modifier = Modifier) {
    val label = when (rule) {
        KeepRule.HIGHER -> Res.string.label_with_advantage
        KeepRule.LOWER -> Res.string.label_with_disadvantage
    }

    Text(text = stringResource(label), style = style, color = rule.color(), modifier = modifier)
}

/** The short form of [KeepRuleLabel], for a line that already reads as a roll. */
@Composable
internal fun KeepRuleShortLabel(rule: KeepRule, style: TextStyle, modifier: Modifier = Modifier) {
    val label = when (rule) {
        KeepRule.HIGHER -> Res.string.btn_advantage
        KeepRule.LOWER -> Res.string.btn_disadvantage
    }

    Text(text = stringResource(label), style = style, color = rule.color(), modifier = modifier)
}

@Composable
internal fun KeepRule.color(): Color = when (this) {
    KeepRule.HIGHER -> LocalHealthColors.current.heal
    KeepRule.LOWER -> MaterialTheme.colorScheme.error
}

@Composable
private fun KeepRuleLabelPreview() {
    Column {
        KeepRule.entries.forEach { KeepRuleLabel(it, MaterialTheme.typography.titleSmall) }
        KeepRule.entries.forEach { KeepRuleShortLabel(it, MaterialTheme.typography.labelMedium) }
    }
}

@Preview
@Composable
private fun PreviewKeepRuleLabelLight() {
    AppThemePreview(darkTheme = false) { KeepRuleLabelPreview() }
}

@Preview
@Composable
private fun PreviewKeepRuleLabelDark() {
    AppThemePreview(darkTheme = true) { KeepRuleLabelPreview() }
}
