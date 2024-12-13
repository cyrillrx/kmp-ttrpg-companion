package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spellbook.data.SampleSpellRepository
import com.cyrillrx.rpg.spellbook.presentation.SpellListState
import com.cyrillrx.rpg.spellbook.presentation.component.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.component.SpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.component.SpellCard
import com.cyrillrx.rpg.spellbook.presentation.component.SpellGrid
import com.cyrillrx.rpg.spellbook.presentation.component.getColor

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    val state = SpellListState.WithData("", SampleSpellRepository().getAll())
    AppTheme(darkTheme = true) {
        AlternativeSpellBookScreen(state) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val state = SpellListState.WithData("", SampleSpellRepository().getAll())
    AppTheme(darkTheme = false) {
        AlternativeSpellBookScreen(state) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    val state = SpellListState.WithData("", SampleSpellRepository().getAll())
    AppTheme(darkTheme = true) {
        SpellBookScreen(state) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenLight() {
    val state = SpellListState.WithData("", SampleSpellRepository().getAll())
    AppTheme(darkTheme = true) {
        SpellBookScreen(state) {}
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
