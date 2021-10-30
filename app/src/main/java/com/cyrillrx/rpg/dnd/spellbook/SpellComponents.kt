package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.ui.theme.AppTheme

@Composable
fun SpellBookScreen(spells: List<Spell>, query: String, applyFilter: (String) -> Unit) {
    AppTheme {
        Column {
            TextField(
                value = query,
                onValueChange = applyFilter,
                modifier = Modifier.fillMaxWidth(),
            )

            LazyRow(modifier = Modifier.fillMaxSize()) {
                items(spells) { spell ->
                    BoxWithConstraints(modifier = Modifier.fillParentMaxSize())
                    {
                        SpellCard(spell)
                    }
                }
            }
        }
    }
}

private val borderStroke = 8.dp
private val textPadding = 8.dp

@Composable
fun SpellCard(spell: Spell, color: Color = Color(173, 29, 29)) {
    Card(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderStroke, color)
    ) {
        Column(Modifier.padding(borderStroke)) {
            Text(
                text = spell.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = textPadding,
                        end = textPadding,
                        top = textPadding / 2,
                        bottom = textPadding / 2
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
                    .background(color)
                    .padding(textPadding / 2),
            )
            SpellGrid(spell, color, borderStroke)
            Text(
                text = spell.content,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(textPadding)
                    .fillMaxSize(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpellGrid(spell: Spell, color: Color, borderStroke: Dp) {
    Column {
        Row(Modifier.background(color)) {
            Column(Modifier.weight(1f)) {
                SpellGridItem("Durée d'incantation", spell.casting_time, color)
                SpellGridItem("composantes", spell.components, color)
            }
            Spacer(Modifier.width(borderStroke))
            Column(Modifier.weight(1f)) {
                SpellGridItem("Portée", spell.range, color)
                SpellGridItem("Durée", spell.duration, color)
            }
        }
    }
}

@Composable
fun SpellGridItem(title: String, subtitle: String, color: Color) {
    Column(Modifier.background(Color.White)) {
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
                .padding(start = textPadding, end = textPadding),
        )

        Text(
            text = subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = textPadding, end = textPadding),
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(borderStroke)
                .background(color)
        )
    }
}

@Preview
@Composable
fun PreviewSpellBookScreen() {
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    SpellBookScreen(items, "Search") {}
}

@Preview
@Composable
fun PreviewSpellCard() {
    val spell = sampleSpell()
    SpellCard(spell)
}

@Preview
@Composable
fun PreviewSpellGrid() {
    val spell = sampleSpell()
    SpellGrid(spell, Color(173, 29, 29), 8.dp)
}

private fun sampleSpell(): Spell {
    val schools = arrayOf("Evocation")
    val levels = arrayOf("1")
    val classes = arrayOf("Clerc", "bard")
    val header = Spell.Header(Spell.Header.Taxonomy(schools, levels, classes))
    return Spell(
        "Fire",
        "descritpô spoks dfpoksdpo ksd",
        1,
        "1 action",
        "36m",
        "VS",
        "1h",
        header
    )
}
