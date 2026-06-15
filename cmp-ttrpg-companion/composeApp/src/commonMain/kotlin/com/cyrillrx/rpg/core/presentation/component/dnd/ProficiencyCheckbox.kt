package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.creature.domain.Proficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.proficiency_expert
import rpg_companion.composeapp.generated.resources.proficiency_none
import rpg_companion.composeapp.generated.resources.proficiency_proficient

@Composable
fun ProficiencyCheckbox(
    proficiency: Proficiency,
    modifier: Modifier = Modifier,
) {
    val (icon, tint) = when (proficiency) {
        Proficiency.NONE -> Icons.Outlined.CheckBoxOutlineBlank to MaterialTheme.colorScheme.onSurfaceVariant
        Proficiency.PROFICIENT -> Icons.Filled.CheckBox to MaterialTheme.colorScheme.primary
        Proficiency.EXPERT -> Icons.Filled.Star to MaterialTheme.colorScheme.tertiary
    }
    val contentDescription = stringResource(
        when (proficiency) {
            Proficiency.NONE -> Res.string.proficiency_none
            Proficiency.PROFICIENT -> Res.string.proficiency_proficient
            Proficiency.EXPERT -> Res.string.proficiency_expert
        },
    )
    Icon(imageVector = icon, contentDescription = contentDescription, tint = tint, modifier = modifier)
}
