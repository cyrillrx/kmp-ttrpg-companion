package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.MarkdownText
import com.cyrillrx.rpg.core.presentation.component.dnd.formatCRValue
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedConditionImmunities
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedDamageAffinities
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedSavingThrows
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedSkills
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.DamageAffinity
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_challenge_rating
import rpg_companion.composeapp.generated.resources.label_condition_immunities
import rpg_companion.composeapp.generated.resources.label_damage_immunities
import rpg_companion.composeapp.generated.resources.label_damage_resistances
import rpg_companion.composeapp.generated.resources.label_damage_vulnerabilities
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_saving_throws
import rpg_companion.composeapp.generated.resources.label_senses
import rpg_companion.composeapp.generated.resources.label_skills

@Composable
fun MonsterDetail(
    monster: Monster,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
) {
    val translation = monster.resolveTranslation(currentLocale())
    val accent = monster.getDisplayType().getColor()

    Column(
        modifier = modifier.padding(spacingCommon),
        verticalArrangement = Arrangement.spacedBy(spacingCommon),
    ) {
        MonsterHeader(monster, translation, accent, titleModifier)

        MainStatLayout(monster, modifier = Modifier.fillMaxWidth())

        val abilities = monster.abilities
        AbilitiesLayout(
            str = abilities.strength.getValueWithModifier(),
            dex = abilities.dexterity.getValueWithModifier(),
            con = abilities.constitution.getValueWithModifier(),
            int = abilities.intelligence.getValueWithModifier(),
            wis = abilities.wisdom.getValueWithModifier(),
            cha = abilities.charisma.getValueWithModifier(),
        )

        MonsterStatBlock(monster, translation)

        MarkdownText(text = translation.description, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun MonsterHeader(
    monster: Monster,
    translation: Monster.Translation,
    accent: Color,
    titleModifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = translation.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = titleModifier,
        )
        translation.subtype?.takeIf { it.isNotBlank() }?.let { subtype ->
            Text(
                text = subtype.capitalize(Locale.current),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = monster.getSubtitle().uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = accent,
        )
        HorizontalDivider(
            color = accent,
            modifier = Modifier.padding(horizontal = spacingCommon, vertical = spacingSmall),
        )
    }
}

@Composable
private fun MonsterStatBlock(monster: Monster, translation: Monster.Translation) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
        StatLine(stringResource(Res.string.label_saving_throws), monster.getFormattedSavingThrows())
        StatLine(stringResource(Res.string.label_skills), monster.getFormattedSkills())
        StatLine(
            stringResource(Res.string.label_damage_resistances),
            monster.getFormattedDamageAffinities(DamageAffinity.RESISTANT),
        )
        StatLine(
            stringResource(Res.string.label_damage_immunities),
            monster.getFormattedDamageAffinities(DamageAffinity.IMMUNE),
        )
        StatLine(
            stringResource(Res.string.label_damage_vulnerabilities),
            monster.getFormattedDamageAffinities(DamageAffinity.VULNERABLE),
        )
        StatLine(stringResource(Res.string.label_condition_immunities), monster.getFormattedConditionImmunities())
        StatLine(stringResource(Res.string.label_senses), translation.senses)
        StatLine(stringResource(Res.string.label_languages), translation.languages.joinToString(", "))
        StatLine(stringResource(Res.string.label_challenge_rating), formatCRValue(monster.challengeRating))
    }
}

@Composable
private fun StatLine(label: String, value: String) {
    if (value.isBlank()) return
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(label)
                append(". ")
            }
            append(value)
        },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview
@Composable
private fun PreviewMonsterDetailLight() {
    AppThemePreview(darkTheme = false) {
        MonsterDetail(monster = SampleMonsterRepository.getFirst())
    }
}

@Preview
@Composable
private fun PreviewMonsterDetailDark() {
    AppThemePreview(darkTheme = true) {
        MonsterDetail(monster = SampleMonsterRepository.getFirst())
    }
}
