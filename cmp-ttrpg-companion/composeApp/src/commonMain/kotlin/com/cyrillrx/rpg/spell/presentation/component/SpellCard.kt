package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.MarkdownText
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedComponents
import com.cyrillrx.rpg.core.presentation.component.dnd.getFormattedSchoolAndLevel
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.spell_casting_time
import rpg_companion.composeapp.generated.resources.spell_components
import rpg_companion.composeapp.generated.resources.spell_duration
import rpg_companion.composeapp.generated.resources.spell_range

@Composable
fun SpellCard(
    spell: Spell,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(spacingSmall),
    content: @Composable ColumnScope.() -> Unit = {
        val translation = spell.resolveTranslation(currentLocale())
        MarkdownText(
            text = translation.description,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(spacingMedium),
        )
    },
) {
    val spellColor = spell.getColor()
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(spacingMedium, spellColor),
    ) {
        Column(Modifier.padding(spacingMedium)) {
            SpellCardHeader(spell)
            content()
        }
    }
}

@Composable
internal fun SpellCardHeader(spell: Spell) {
    val translation = spell.resolveTranslation(currentLocale())
    val spellColor = spell.getColor()
    Text(
        text = translation.name,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = spacingMedium,
                end = spacingMedium,
                top = spacingMedium / 2,
                bottom = spacingMedium / 2,
            ),
    )
    Text(
        text = spell.getFormattedSchoolAndLevel(),
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .background(spellColor)
            .padding(spacingMedium / 2),
    )
    SpellGrid(spell, spellColor, spacingMedium)
}

@Composable
private fun SpellGrid(spell: Spell, spellColor: Color, spacingMedium: Dp) {
    val translation = spell.resolveTranslation(currentLocale())

    Column {
        Row(Modifier.background(spellColor)) {
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    label = stringResource(Res.string.spell_casting_time),
                    value = translation.castingTime,
                    spellColor = spellColor,
                )
                SpellGridItem(
                    label = stringResource(Res.string.spell_components),
                    value = spell.getFormattedComponents(),
                    spellColor = spellColor,
                )
            }
            Spacer(Modifier.width(spacingMedium))
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    label = stringResource(Res.string.spell_range),
                    value = translation.range,
                    spellColor = spellColor,
                )
                SpellGridItem(
                    label = stringResource(Res.string.spell_duration),
                    value = translation.duration,
                    spellColor = spellColor,
                )
            }
        }
    }
}

@Composable
private fun SpellGridItem(label: String, value: String, spellColor: Color) {
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = spellColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacingMedium, end = spacingMedium),
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacingMedium, end = spacingMedium),
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(spacingMedium)
                .background(spellColor),
        )
    }
}

@Preview
@Composable
fun PreviewSpellCardLight() {
    SpellCardPreview(darkTheme = false)
}

@Preview
@Composable
fun PreviewSpellCardDark() {
    SpellCardPreview(darkTheme = true)
}

@Preview
@Composable
fun PreviewSpellGridLight() {
    SpellGridPreview(darkTheme = false)
}

@Preview
@Composable
fun PreviewSpellGridDark() {
    SpellGridPreview(darkTheme = true)
}

@Composable
private fun SpellCardPreview(darkTheme: Boolean) {
    val spell = SampleSpellRepository.getFirst()
    AppThemePreview(darkTheme = darkTheme) {
        SpellCard(spell)
    }
}

@Composable
private fun SpellGridPreview(darkTheme: Boolean) {
    val spell = SampleSpellRepository.getFirst()

    AppThemePreview(darkTheme = darkTheme) {
        SpellGrid(
            spell = spell,
            spellColor = spell.getColor(),
            spacingMedium = spacingMedium,
        )
    }
}
