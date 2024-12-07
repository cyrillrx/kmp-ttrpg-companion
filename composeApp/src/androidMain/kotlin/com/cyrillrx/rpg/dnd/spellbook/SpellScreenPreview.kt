package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spellbook.data.SampleSpellRepository
import com.cyrillrx.rpg.spellbook.data.api.ApiSpell
import com.cyrillrx.rpg.spellbook.presentation.AlternativeSpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellBookScreen
import com.cyrillrx.rpg.spellbook.presentation.SpellCard
import com.cyrillrx.rpg.spellbook.presentation.SpellGrid
import com.cyrillrx.rpg.spellbook.presentation.getColor
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.formatted_spell_school_level
import rpg_companion.composeapp.generated.resources.spell_search_hint

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

@Composable
fun ApiSpell.getFormattedSchool() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)
