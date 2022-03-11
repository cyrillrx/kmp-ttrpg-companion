package com.cyrillrx.rpg.dnd.spellbook.widget

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.cyrillrx.rpg.ui.theme.AppTheme
import com.cyrillrx.rpg.ui.theme.spacingMedium
import com.cyrillrx.rpg.ui.widget.OverflowMenu
import com.cyrillrx.rpg.ui.widget.Search
import de.charlex.compose.HtmlText

internal val borderStroke = spacingMedium
internal val textPadding = spacingMedium

@Composable
fun SpellBookScreen(spells: List<Spell>, query: String, applyFilter: (String) -> Unit) {
    Column {
        Search(
            query = query,
            applyFilter = applyFilter,
        ) { Text(stringResource(id = R.string.spell_search_hint)) }

        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(spells) { spell ->
                BoxWithConstraints(modifier = Modifier.fillParentMaxSize()) {
                    SpellCard(spell)
                }
            }
        }
    }
}

@Composable
fun SpellBookPeekScreen(
    spells: List<Spell>,
    savedSpell: List<Spell>,
    query: String,
    savedSpellsOnly: Boolean,
    applyFilter: (String) -> Unit,
    onDisplaySavedOnlyClicked: (Boolean) -> Unit,
    onSaveClicked: (Spell) -> Unit,
    navigateToSpell: (Spell) -> Unit,
) {
    AppTheme {
        Column {
            SearchBarWithOverflow(
                query = query,
                savedSpellsOnly = savedSpellsOnly,
                applyFilter = applyFilter,
                onDisplaySavedSpellsClicked = onDisplaySavedOnlyClicked,
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(spells) { spell ->

                    SpellListItem(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { navigateToSpell(spell) },
                        spell,
                        savedSpell,
                        onSaveClicked,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBarWithOverflow(
    query: String,
    savedSpellsOnly: Boolean,
    applyFilter: (String) -> Unit,
    onDisplaySavedSpellsClicked: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Search(
            modifier = Modifier.weight(1f),
            query = query,
            applyFilter = applyFilter
        ) {
            Text(stringResource(id = R.string.spell_search_hint))
        }
        OverflowMenu {
            DropdownMenuItem(onClick = { onDisplaySavedSpellsClicked(!savedSpellsOnly) }) {
                Row {
                    Text(text = "Bookmarked")
                    Checkbox(savedSpellsOnly, onDisplaySavedSpellsClicked)
                }
            }
        }
    }
}

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
            HtmlText(
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
fun PreviewSpellBookScreenDark() {
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = true) {
        SpellBookScreen(items, stringResource(id = R.string.spell_search_hint)) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = false) {
        SpellBookScreen(items, stringResource(id = R.string.spell_search_hint)) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = true) {
        SpellBookPeekScreen(
            items,
            items,
            stringResource(id = R.string.spell_search_hint),
            false,
            {},
            {},
            {},
            {},
        )
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenLight() {
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = false) {
        SpellBookPeekScreen(
            items,
            items,
            stringResource(id = R.string.spell_search_hint),
            false,
            {},
            {},
            {},
            {},
        )
    }
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

internal fun Spell.getColor(): Color = Color(173, 29, 29)

@Composable
fun Spell.getFormattedSchool() =
    stringResource(R.string.formatted_spell_school_level, getSchool(), level)
