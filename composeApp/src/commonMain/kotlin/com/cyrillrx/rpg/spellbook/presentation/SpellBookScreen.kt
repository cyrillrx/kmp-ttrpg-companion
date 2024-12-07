package com.cyrillrx.rpg.spellbook.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.core.presentation.OverflowMenu
import com.cyrillrx.rpg.core.presentation.Search
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spellbook.data.SampleSpellRepository
import com.cyrillrx.rpg.spellbook.data.api.ApiSpell
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.formatted_spell_school_level
import rpg_companion.composeapp.generated.resources.spell_casting_time
import rpg_companion.composeapp.generated.resources.spell_components
import rpg_companion.composeapp.generated.resources.spell_duration
import rpg_companion.composeapp.generated.resources.spell_range
import rpg_companion.composeapp.generated.resources.spell_search_hint

@Composable
fun SpellBookScreen(
    viewModel: SpellBookViewModel,
    navigateToSpell: (ApiSpell) -> Unit,
) {
    SpellBookScreen(
        viewModel.displayedSpells,
        viewModel.savedSpells,
        viewModel.query,
        viewModel.savedSpellsOnly,
        viewModel::applyFilter,
        viewModel::onDisplaySavedOnlyClicked,
        viewModel::onSaveSpell,
        navigateToSpell,
    )
}

@Composable
fun SpellBookScreen(
    spells: List<ApiSpell>,
    savedSpell: List<ApiSpell>,
    query: String,
    savedSpellsOnly: Boolean,
    applyFilter: (String) -> Unit,
    onDisplaySavedOnlyClicked: (Boolean) -> Unit,
    onSaveClicked: (ApiSpell) -> Unit,
    navigateToSpell: (ApiSpell) -> Unit,
) {
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

@Composable
fun AlternativeSpellBookScreen(viewModel: SpellBookViewModel) {
    AlternativeSpellBookScreen(
        viewModel.displayedSpells,
        viewModel.query,
        viewModel::applyFilter,
    )
}

@Composable
fun AlternativeSpellBookScreen(
    spells: List<ApiSpell>,
    query: String,
    applyFilter: (String) -> Unit,
) {
    Column {
        Search(
            query = query,
            applyFilter = applyFilter,
        ) { Text(stringResource(Res.string.spell_search_hint)) }

        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(spells) { spell ->
                Box(modifier = Modifier.fillParentMaxSize()) {
                    SpellCard(spell)
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
            applyFilter = applyFilter,
        ) {
            Text(stringResource(Res.string.spell_search_hint))
        }
        OverflowMenu {
            DropdownMenuItem(
                onClick = { onDisplaySavedSpellsClicked(!savedSpellsOnly) },
                text = {
                    Row {
                        Text(text = "Bookmarked")
                        Checkbox(savedSpellsOnly, onDisplaySavedSpellsClicked)
                    }
                },
            )
        }
    }
}

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
            Text(
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

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    val spell = SampleSpellRepository().get()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = true) {
        AlternativeSpellBookScreen(items, stringResource(Res.string.spell_search_hint)) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val spell = SampleSpellRepository().get()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = false) {
        AlternativeSpellBookScreen(items, stringResource(Res.string.spell_search_hint)) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    val spell = SampleSpellRepository().get()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = true) {
        SpellBookScreen(
            items,
            items,
            stringResource(Res.string.spell_search_hint),
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
    val spell = SampleSpellRepository().get()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = false) {
        SpellBookScreen(
            items,
            items,
            stringResource(Res.string.spell_search_hint),
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
    val spell = SampleSpellRepository().get()
    SpellCard(spell)
}

@Preview
@Composable
fun PreviewSpellGrid() {
    val spell = SampleSpellRepository().get()
    SpellGrid(spell, spell.getColor(), spacingMedium)
}

fun ApiSpell.getColor(): Color = Color(173, 29, 29)

@Composable
fun ApiSpell.getFormattedSchool() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)
