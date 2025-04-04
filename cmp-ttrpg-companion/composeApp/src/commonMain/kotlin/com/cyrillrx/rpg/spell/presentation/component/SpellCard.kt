package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.core.presentation.component.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.formatted_spell_school_level
import rpg_companion.composeapp.generated.resources.school_abjuration
import rpg_companion.composeapp.generated.resources.school_conjuration
import rpg_companion.composeapp.generated.resources.school_divination
import rpg_companion.composeapp.generated.resources.school_enchantment
import rpg_companion.composeapp.generated.resources.school_evocation
import rpg_companion.composeapp.generated.resources.school_illusion
import rpg_companion.composeapp.generated.resources.school_necromancy
import rpg_companion.composeapp.generated.resources.school_transmutation
import rpg_companion.composeapp.generated.resources.spell_casting_time
import rpg_companion.composeapp.generated.resources.spell_components
import rpg_companion.composeapp.generated.resources.spell_duration
import rpg_companion.composeapp.generated.resources.spell_range

@Composable
fun SpellCard(spell: Spell, modifier: Modifier = Modifier) {
    val spellColor = spell.getColor()
    Card(
        modifier = modifier
            .padding(spacingSmall)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(spacingMedium, spellColor),
    ) {
        Column(
            Modifier
                .padding(spacingMedium)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Text(
                text = spell.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = spacingMedium,
                        end = spacingMedium,
                        top = spacingMedium / 2,
                        bottom = spacingMedium / 2,
                    ),
            )
            Text(
                text = spell.getFormattedSchool(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(spellColor)
                    .padding(spacingMedium / 2),
            )
            SpellGrid(spell, spellColor, spacingMedium)
            HtmlText(
                text = spell.description,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(spacingMedium)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SpellGrid(spell: Spell, spellColor: Color, spacingMedium: Dp) {
    Column {
        Row(Modifier.background(spellColor)) {
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    title = stringResource(Res.string.spell_casting_time),
                    subtitle = spell.castingTime,
                    spellColor = spellColor,
                )
                SpellGridItem(
                    title = stringResource(Res.string.spell_components),
                    subtitle = spell.components,
                    spellColor = spellColor,
                )
            }
            Spacer(Modifier.width(spacingMedium))
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    title = stringResource(Res.string.spell_range),
                    subtitle = spell.range,
                    spellColor = spellColor,
                )
                SpellGridItem(
                    title = stringResource(Res.string.spell_duration),
                    subtitle = spell.duration,
                    spellColor = spellColor,
                )
            }
        }
    }
}

@Composable
private fun SpellGridItem(title: String, subtitle: String, spellColor: Color) {
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = spellColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = spacingMedium, end = spacingMedium),
        )

        Text(
            text = subtitle,
            fontSize = 16.sp,
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

fun Spell.getColor(): Color = Color(173, 29, 29)

@Composable
fun Spell.getFormattedSchool() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)

@Composable
private fun Spell.getSchool(): String {
    return schools.map { it.toFormattedString() }.joinToString(", ")
}

@Composable
fun Spell.School.toFormattedString(): String {
    val stringRes = when (this) {
        Spell.School.ABJURATION -> Res.string.school_abjuration
        Spell.School.CONJURATION -> Res.string.school_conjuration
        Spell.School.DIVINATION -> Res.string.school_divination
        Spell.School.ENCHANTMENT -> Res.string.school_enchantment
        Spell.School.EVOCATION -> Res.string.school_evocation
        Spell.School.ILLUSION -> Res.string.school_illusion
        Spell.School.NECROMANCY -> Res.string.school_necromancy
        Spell.School.TRANSMUTATION -> Res.string.school_transmutation
    }
    return stringResource(stringRes)
}

@Preview
@Composable
private fun PreviewSpellCard() {
    val spell = SampleSpellRepository().get()
    SpellCard(spell)
}

@Preview
@Composable
private fun PreviewSpellGrid() {
    val spell = SampleSpellRepository().get()
    SpellGrid(spell, spell.getColor(), spacingMedium)
}
