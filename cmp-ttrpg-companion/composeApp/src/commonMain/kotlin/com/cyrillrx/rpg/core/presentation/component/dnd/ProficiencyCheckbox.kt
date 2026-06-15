package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.creature.domain.Proficiency

@Composable
fun ProficiencyCheckbox(
    proficiency: Proficiency,
    modifier: Modifier = Modifier,
) {
    val icon = when (proficiency) {
        Proficiency.NONE -> Icons.Outlined.CheckBoxOutlineBlank
        Proficiency.PROFICIENT -> Icons.Filled.CheckBox
        Proficiency.EXPERT -> Icons.Filled.Star
    }
    Icon(
        imageVector = icon,
        contentDescription = proficiency.toFormattedString(),
        tint = proficiency.getColor(),
        modifier = modifier,
    )
}
