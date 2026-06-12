package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.character.domain.MAX_ABILITY_SCORE
import com.cyrillrx.rpg.character.domain.MIN_ABILITY_SCORE
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_ability
import rpg_companion.composeapp.generated.resources.label_saving_throw_proficiency
import rpg_companion.composeapp.generated.resources.proficiency_expert
import rpg_companion.composeapp.generated.resources.proficiency_none
import rpg_companion.composeapp.generated.resources.proficiency_proficient

@Composable
internal fun AbilityEditDialog(
    title: String,
    initialAbility: Ability,
    onConfirm: (Ability) -> Unit,
    onDismiss: () -> Unit,
) {
    var value by remember(initialAbility) {
        mutableIntStateOf(initialAbility.value.coerceIn(MIN_ABILITY_SCORE, MAX_ABILITY_SCORE))
    }
    var proficiency by remember(initialAbility) { mutableStateOf(initialAbility.savingThrowProficiency) }

    EditDialog(
        title = title,
        subtitle = stringResource(
            Res.string.hint_ability,
            MIN_ABILITY_SCORE,
            MAX_ABILITY_SCORE,
            initialAbility.getValueWithModifier(),
        ),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(initialAbility.copy(value = value, savingThrowProficiency = proficiency)) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth(),
        ) {
            DecrementIncrementRow(
                value = value,
                minValue = MIN_ABILITY_SCORE,
                maxValue = MAX_ABILITY_SCORE,
                onDecrement = { value-- },
                onIncrement = { value++ },
            )
            Text(
                text = Ability.computeModifier(value).toSignedString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val (icon, tint) = when (proficiency) {
                Proficiency.NONE -> Icons.Outlined.CheckBoxOutlineBlank to MaterialTheme.colorScheme.onSurfaceVariant
                Proficiency.PROFICIENT -> Icons.Filled.CheckBox to MaterialTheme.colorScheme.primary
                Proficiency.EXPERT -> Icons.Filled.Star to MaterialTheme.colorScheme.tertiary
            }
            val stateLabel = stringResource(
                when (proficiency) {
                    Proficiency.NONE -> Res.string.proficiency_none
                    Proficiency.PROFICIENT -> Res.string.proficiency_proficient
                    Proficiency.EXPERT -> Res.string.proficiency_expert
                },
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(role = Role.Button) { proficiency = proficiency.next() }
                    .padding(vertical = spacingMedium),
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint)
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.label_saving_throw_proficiency))
                        append(": ")
                        withStyle(SpanStyle(color = tint)) { append(stateLabel) }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = spacingMedium),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAbilityEditDialogLight() {
    AppThemePreview(darkTheme = false) {
        AbilityEditDialog(
            title = "Strength",
            initialAbility = Ability(16, Proficiency.PROFICIENT),
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun PreviewAbilityEditDialogDark() {
    AppThemePreview(darkTheme = true) {
        AbilityEditDialog(
            title = "Strength",
            initialAbility = Ability(16, Proficiency.PROFICIENT),
            onConfirm = {},
            onDismiss = {},
        )
    }
}
