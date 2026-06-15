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
    Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = modifier)
}
