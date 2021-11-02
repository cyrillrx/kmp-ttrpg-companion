package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.ui.component.Search
import com.cyrillrx.rpg.ui.theme.AppTheme

@Composable
fun SpellBookScreen(spells: List<Spell>, query: String, applyFilter: (String) -> Unit) {
    AppTheme {
        Column {
            Search(query, applyFilter) { Text(stringResource(id = R.string.spell_search_hint)) }

            LazyRow(modifier = Modifier.fillMaxSize()) {
                items(spells) { spell ->
                    BoxWithConstraints(modifier = Modifier.fillParentMaxSize()) {
                        SpellCard(spell)
                    }
                }
            }
        }
    }
}

@Composable
fun SpellBookPeekScreen(
    spells: List<Spell>,
    query: String,
    applyFilter: (String) -> Unit,
    navigateToSpell: (Spell) -> Unit,
) {
    AppTheme {
        Column {
            Search(query, applyFilter) { Text(stringResource(id = R.string.spell_search_hint)) }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(spells) { spell ->

                    SpellListItem(spell, Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable { navigateToSpell(spell) })
                }
            }
        }
    }
}


@Composable
fun SpellListItem(spell: Spell, modifier: Modifier) {
    val spellColor = spell.getColor()

    Card(
        shape = CutCornerShape(0.dp),
        modifier = modifier.padding(4.dp),
    ) {
        Column {
            Text(
                text = spell.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(spellColor)
                    .padding(
                        start = textPadding,
                        end = textPadding,
                        top = textPadding / 2,
                    ),
            )
            Text(
                text = spell.getFormattedSchool(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(spellColor)
                    .padding(
                        start = textPadding,
                        end = textPadding,
                        bottom = textPadding / 2,
                    ),
            )
            SpellGrid2(
                spell,
                Modifier.padding(
                    horizontal = textPadding,
                    vertical = textPadding / 2,
                ),
            )
            Text(
                text = spell.content,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                modifier = Modifier
                    .padding(textPadding)
                    .fillMaxSize(),
            )
        }
    }
}

private val borderStroke = 8.dp
private val textPadding = 8.dp

@Composable
fun SpellCard(spell: Spell) {
    val spellColor = spell.getColor()
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderStroke, spellColor)
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
                    .background(spellColor)
                    .padding(textPadding / 2),
            )
            SpellGrid(spell, spellColor, borderStroke)
            Text(
                text = spell.content,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(textPadding)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
fun SpellGrid(spell: Spell, color: Color, borderStroke: Dp) {
    Column {
        Row(Modifier.background(color)) {
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    title = stringResource(R.string.spell_casting_time),
                    subtitle = spell.casting_time,
                    color = color,
                )
                SpellGridItem(
                    title = stringResource(R.string.spell_components),
                    subtitle = spell.components,
                    color = color,
                )
            }
            Spacer(Modifier.width(borderStroke))
            Column(Modifier.weight(1f)) {
                SpellGridItem(
                    title = stringResource(R.string.spell_range),
                    subtitle = spell.range,
                    color = color,
                )
                SpellGridItem(
                    title = stringResource(R.string.spell_duration),
                    subtitle = spell.duration,
                    color = color,
                )
            }
        }
    }
}

@Composable
fun SpellGrid2(spell: Spell, modifier: Modifier) {
    Column(modifier) {
        Row {
            Text(
                text = stringResource(R.string.formatted_spell_casting_time),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.casting_time, modifier = Modifier.padding(start = 4.dp))
        }
        Row {
            Text(
                text = stringResource(R.string.formatted_spell_components),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.components, modifier = Modifier.padding(start = 4.dp))
        }
        Row {
            Text(
                text = stringResource(R.string.formatted_spell_range),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.range, modifier = Modifier.padding(start = 4.dp))
        }
        Row {

            Text(
                text = stringResource(R.string.formatted_spell_duration),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.duration, modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@Composable
fun SpellGridItem(title: String, subtitle: String, color: Color) {
    Column(Modifier.background(MaterialTheme.colors.surface)) {
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
    SpellBookScreen(items, stringResource(id = R.string.spell_search_hint)) {}
}

@Preview
@Composable
fun PreviewSpellBookPeekScreen() {
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    SpellBookPeekScreen(items, stringResource(id = R.string.spell_search_hint), {}) {}
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
    SpellGrid(spell, spell.getColor(), borderStroke)
}

private fun Spell.getColor(): Color = Color(173, 29, 29)

@Composable
fun Spell.getFormattedSchool() =
    stringResource(R.string.formatted_spell_school_level, getSchool(), level)


private fun sampleSpell(): Spell {
    val schools = arrayOf("Evocation")
    val levels = arrayOf("3")
    val classes = arrayOf("Ensorceleur/Sorcelame", "Magicien")
    val header = Spell.Header(Spell.Header.Taxonomy(schools, levels, classes))
    return Spell(
        "Fire",
        "Une traînée luisante part de votre doigt tendu et file vers un point de votre choix situé à portée et dans votre champ de vision, où elle explose dans une gerbe de flammes grondantes. Chaque créature située dans une sphère de 6 mètres de rayon centrée sur ce point doit faire un jet de sauvegarde de Dextérité. Celles qui échouent subissent 8d6 dégâts de feu, les autres la moitié seulement.\n" +
                "\n" +
                "Le feu s'étend en contournant les angles. Il embrase les objets inflammables de la zone, à moins que quelqu'un ne les porte ou ne les transporte.\n" +
                "\n" +
                "À plus haut niveau. Si vous lancez ce sort en utilisant un emplacement de niveau 4 ou supérieur, les dégâts augmentent de 1d6 par niveau au-delà du niveau 3.",
        3,
        "1 action",
        "45 mètres",
        "V, S, M (une petite boule de guano de chauve-souris et du soufre)",
        "instantanée",
        header
    )
}
