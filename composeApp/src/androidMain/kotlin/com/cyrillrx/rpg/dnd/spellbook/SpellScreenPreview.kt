package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellListState
import com.cyrillrx.rpg.spell.presentation.component.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellBookScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellCard
import com.cyrillrx.rpg.spell.presentation.component.SpellGrid
import com.cyrillrx.rpg.spell.presentation.component.getColor

private val stateWithSampleData = SpellListState(
    searchQuery = "",
    body = SpellListState.Body.WithData(SampleSpellRepository().getAll()),
)

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    AppTheme(darkTheme = true) {
        AlternativeSpellBookScreen(stateWithSampleData) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    AppTheme(darkTheme = false) {
        AlternativeSpellBookScreen(stateWithSampleData) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    AppTheme(darkTheme = true) {
        SpellBookScreen(stateWithSampleData, {}, {}, {})
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenLight() {
    AppTheme(darkTheme = true) {
        SpellBookScreen(stateWithSampleData, {}, {}, {})
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
