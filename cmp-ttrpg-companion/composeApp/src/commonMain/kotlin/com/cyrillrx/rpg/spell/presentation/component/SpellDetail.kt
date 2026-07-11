package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.MarkdownText
import com.cyrillrx.rpg.core.presentation.component.TintedTag
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedComponents
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedSchoolAndLevel
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.spell_casting_time
import rpg_companion.composeapp.generated.resources.spell_components
import rpg_companion.composeapp.generated.resources.spell_concentration
import rpg_companion.composeapp.generated.resources.spell_duration
import rpg_companion.composeapp.generated.resources.spell_material_components
import rpg_companion.composeapp.generated.resources.spell_range
import rpg_companion.composeapp.generated.resources.spell_ritual

private const val MATERIAL_MARKER = "*"

@Composable
fun SpellDetail(
    spell: Spell,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
) {
    val translation = spell.resolveTranslation(currentLocale())
    val accent = spell.getColor()

    Column(
        modifier = modifier.padding(spacingCommon),
        verticalArrangement = Arrangement.spacedBy(spacingCommon),
    ) {
        SpellHeader(spell, accent, titleModifier)

        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                SpellMetaCell(
                    Icons.Outlined.Schedule,
                    stringResource(Res.string.spell_casting_time),
                    translation.castingTime,
                    accent,
                )
                SpellMetaCell(
                    Icons.Outlined.Straighten,
                    stringResource(Res.string.spell_range),
                    translation.range,
                    accent,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                SpellMetaCell(
                    Icons.Outlined.Category,
                    stringResource(Res.string.spell_components),
                    spell.getFormattedComponents() + if (spell.components.material) MATERIAL_MARKER else "",
                    accent,
                )
                SpellMetaCell(
                    Icons.Outlined.HourglassEmpty,
                    stringResource(Res.string.spell_duration),
                    translation.duration,
                    accent,
                )
            }
        }

        translation.materialDescription?.takeIf { it.isNotBlank() }?.let { material ->
            val label = stringResource(Res.string.spell_material_components)
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$MATERIAL_MARKER ")
                        append(label)
                        append(". ")
                    }
                    append(material)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        MarkdownText(text = translation.description, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun SpellHeader(spell: Spell, accent: Color, titleModifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = spell.resolveTranslation(currentLocale()).name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = titleModifier,
        )
        Text(
            text = spell.toFormattedSchoolAndLevel().uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = accent,
        )
        HorizontalDivider(
            color = accent,
            modifier = Modifier.padding(horizontal = spacingCommon, vertical = spacingSmall),
        )
        val tags = buildList {
            if (spell.ritual) add(stringResource(Res.string.spell_ritual))
            if (spell.concentration) add(stringResource(Res.string.spell_concentration))
        }
        if (tags.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacingSmall)) {
                tags.forEach { TintedTag(it, accent) }
            }
        }
    }
}

@Composable
private fun RowScope.SpellMetaCell(
    icon: ImageVector,
    label: String,
    value: String,
    accent: Color,
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingMedium),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(iconSizeMedium),
            )
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSpellDetailLight() {
    AppThemePreview(darkTheme = false) {
        SpellDetail(SampleSpellRepository.fireball())
    }
}

@Preview
@Composable
private fun PreviewSpellDetailDark() {
    AppThemePreview(darkTheme = true) {
        SpellDetail(SampleSpellRepository.fireball())
    }
}
