package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.LocalHealthColors
import com.cyrillrx.rpg.dice.domain.KeepRule
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.label_with_advantage
import rpg_companion.app.shared.ui.generated.resources.label_with_disadvantage

@Composable
fun KeepRuleLabel(rule: KeepRule, style: TextStyle, modifier: Modifier = Modifier) {
    when (rule) {
        KeepRule.HIGHER -> Text(
            text = stringResource(Res.string.label_with_advantage),
            style = style,
            color = LocalHealthColors.current.heal,
            modifier = modifier,
        )

        KeepRule.LOWER -> Text(
            text = stringResource(Res.string.label_with_disadvantage),
            style = style,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun PreviewKeepRuleLabelLight() {
    AppThemePreview(darkTheme = false) { KeepRuleLabel(KeepRule.HIGHER, MaterialTheme.typography.titleSmall) }
}

@Preview
@Composable
private fun PreviewKeepRuleLabelDark() {
    AppThemePreview(darkTheme = true) { KeepRuleLabel(KeepRule.LOWER, MaterialTheme.typography.titleSmall) }
}
