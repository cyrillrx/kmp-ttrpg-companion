package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.dnd.relatedAbilityAbbreviation
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.computeModifier
import com.cyrillrx.rpg.creature.domain.getProficiency
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_skills

@Composable
internal fun SkillsSection(
    skills: Skills,
    abilities: Abilities,
    proficiencyBonus: Int,
    onTap: () -> Unit,
) {
    Card(
        onClick = onTap,
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingCommon, vertical = spacingMedium),
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {
            Text(
                text = stringResource(Res.string.label_skills),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val half = (Skill.entries.size + 1) / 2
            val leftColumn = Skill.entries.take(half)
            val rightColumn = Skill.entries.drop(half)
            leftColumn.zip(rightColumn).forEach { (left, right) ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    SkillEntry(
                        skill = left,
                        skills = skills,
                        abilities = abilities,
                        proficiencyBonus = proficiencyBonus,
                        modifier = Modifier.weight(1f),
                    )
                    SkillEntry(
                        skill = right,
                        skills = skills,
                        abilities = abilities,
                        proficiencyBonus = proficiencyBonus,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillEntry(
    skill: Skill,
    skills: Skills,
    abilities: Abilities,
    proficiencyBonus: Int,
    modifier: Modifier = Modifier,
) {
    val proficiency = skill.getProficiency(skills)
    val isProficient = proficiency != Proficiency.NONE
    val bonus = skill.computeModifier(abilities, proficiency, proficiencyBonus)
    val color = if (isProficient) MaterialTheme.colorScheme.primary else Color.Unspecified
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = modifier,
    ) {
        Text(
            text = bonus.toSignedString(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isProficient) FontWeight.Bold else FontWeight.Normal,
            color = color,
        )
        Text(
            text = "${skill.toFormattedString()} (${skill.relatedAbilityAbbreviation()})",
            style = MaterialTheme.typography.bodySmall,
            color = color,
        )
    }
}
