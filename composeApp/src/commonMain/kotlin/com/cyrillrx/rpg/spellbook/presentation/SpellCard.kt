package com.cyrillrx.rpg.spellbook.presentation

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
import com.cyrillrx.rpg.core.presentation.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spellbook.data.api.ApiSpell
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.formatted_spell_school_level
import rpg_companion.composeapp.generated.resources.spell_casting_time
import rpg_companion.composeapp.generated.resources.spell_components
import rpg_companion.composeapp.generated.resources.spell_duration
import rpg_companion.composeapp.generated.resources.spell_range

@Composable
fun SpellCard(spell: ApiSpell) {
    val spellColor = spell.getColor()
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(spacingMedium, spellColor),
    ) {
        Column(Modifier.padding(spacingMedium)) {
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
                text = spell.content,
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
fun SpellGrid(spell: ApiSpell, color: Color, spacingMedium: Dp) {
    Column {
        Row(Modifier.background(color)) {
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    title = stringResource(Res.string.spell_casting_time),
                    subtitle = spell.casting_time,
                    color = color,
                )
                SpellGridItem(
                    title = stringResource(Res.string.spell_components),
                    subtitle = spell.components,
                    color = color,
                )
            }
            Spacer(Modifier.width(spacingMedium))
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    title = stringResource(Res.string.spell_range),
                    subtitle = spell.range,
                    color = color,
                )
                SpellGridItem(
                    title = stringResource(Res.string.spell_duration),
                    subtitle = spell.duration,
                    color = color,
                )
            }
        }
    }
}

@Composable
fun SpellGridItem(title: String, subtitle: String, color: Color) {
    Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = color,
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
                .background(color),
        )
    }
}

fun ApiSpell.getColor(): Color = Color(173, 29, 29)

@Composable
fun ApiSpell.getFormattedSchool() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)
