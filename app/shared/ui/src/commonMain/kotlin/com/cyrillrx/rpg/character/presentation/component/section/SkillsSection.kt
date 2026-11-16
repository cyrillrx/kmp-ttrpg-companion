package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.skillModifier
import com.cyrillrx.rpg.character.domain.skillProficiency
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.relatedAbilityAbbreviation
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toStringRes
import com.cyrillrx.rpg.core.presentation.component.sortedByLocalizedName
import com.cyrillrx.rpg.core.presentation.format.getFontWeight
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.skillModifierMinWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.creature.domain.Skills
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.app.shared.ui.generated.resources.Res
import rpg_companion.app.shared.ui.generated.resources.btn_roll

@Composable
internal fun SkillsSection(
    character: Character,
    onSkillTapped: (Skill) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val sortedSkills = Skill.entries.sortedByLocalizedName { it.toStringRes() }
        val skillColumns = sortedSkills.chunked((sortedSkills.size + 1) / 2)
        val leftColumn = skillColumns.getOrElse(0) { emptyList() }
        val rightColumn = skillColumns.getOrElse(1) { emptyList() }
        leftColumn.forEachIndexed { index, left ->
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                SkillEntry(
                    skill = left,
                    character = character,
                    onRollClicked = { onSkillTapped(left) },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                val right = rightColumn.getOrNull(index)
                if (right == null) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    SkillEntry(
                        skill = right,
                        character = character,
                        onRollClicked = { onSkillTapped(right) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillEntry(
    skill: Skill,
    character: Character,
    onRollClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val proficiency = character.skillProficiency(skill)
    val color = proficiency.getColor()
    val fontWeight = proficiency.getFontWeight()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(Res.string.btn_roll),
                onClick = onRollClicked,
            )
            .minimumInteractiveComponentSize()
            .padding(horizontal = spacingMedium),
    ) {
        Text(
            text = character.skillModifier(skill).toSignedString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = fontWeight,
            color = color,
            modifier = Modifier.widthIn(min = skillModifierMinWidth),
        )
        Text(
            text = skillLabel(skill),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = fontWeight,
            color = color,
        )
    }
}

@Composable
private fun skillLabel(skill: Skill): AnnotatedString {
    val name = skill.toFormattedString()
    val abbreviation = skill.relatedAbilityAbbreviation()
    val abbreviationStyle = SpanStyle(
        fontSize = MaterialTheme.typography.labelSmall.fontSize,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    return buildAnnotatedString {
        append(name)
        append(' ')
        withStyle(abbreviationStyle) { append("($abbreviation)") }
    }
}

@Preview
@Composable
private fun PreviewSkillsSectionLight() {
    AppThemePreview(darkTheme = false) {
        SkillsSection(character = previewCharacter(), onSkillTapped = {})
    }
}

@Preview
@Composable
private fun PreviewSkillsSectionDark() {
    AppThemePreview(darkTheme = true) {
        SkillsSection(character = previewCharacter(), onSkillTapped = {})
    }
}

private fun previewCharacter(): Character = SampleCharacterRepository.elfRogue()
    .copy(skills = Skills(acrobatics = Proficiency.PROFICIENT, perception = Proficiency.EXPERT))
