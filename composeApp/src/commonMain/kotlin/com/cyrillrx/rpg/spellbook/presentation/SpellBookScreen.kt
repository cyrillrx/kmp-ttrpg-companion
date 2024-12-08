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
