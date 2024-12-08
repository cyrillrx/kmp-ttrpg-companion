package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spellbook.data.SampleSpellRepository
import com.cyrillrx.rpg.spellbook.presentation.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellCard
import com.cyrillrx.rpg.spellbook.presentation.SpellGrid
import com.cyrillrx.rpg.spellbook.presentation.SpellListState
import com.cyrillrx.rpg.spellbook.presentation.getColor

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    val state = SpellListState(searchResults = SampleSpellRepository().getAll())
    AppTheme(darkTheme = true) {
        AlternativeSpellBookScreen(state, {})
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val state = SpellListState(searchResults = SampleSpellRepository().getAll())
    AppTheme(darkTheme = false) {
        AlternativeSpellBookScreen(state, {})
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    val state = SpellListState(searchResults = SampleSpellRepository().getAll())
    AppTheme(darkTheme = true) {
        SpellBookScreen(state = state, onAction = { })
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenLight() {
    val state = SpellListState(
        searchResults = SampleSpellRepository().getAll(),
    )
    AppTheme(darkTheme = true) {
        SpellBookScreen(state = state, onAction = { })
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
