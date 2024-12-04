package com.cyrillrx.rpg.dnd.spellbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.api.spellbook.ApiSpell
import com.cyrillrx.rpg.common.theme.AppTheme
import com.cyrillrx.rpg.common.theme.spacingMedium
import com.cyrillrx.rpg.spellbook.data.SampleSpellRepository
import com.cyrillrx.rpg.spellbook.data.sampleSpell
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
    val spell = sampleSpell()
    val items = listOf(spell, spell, spell)
    AppTheme(darkTheme = false) {
        AlternativeSpellBookScreen(items, stringResource(Res.string.spell_search_hint)) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookPeekScreenDark() {
    val spell = sampleSpell()
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
    val spell = sampleSpell()
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
    val spell = sampleSpell()
    SpellCard(spell)
}

@Preview
@Composable
fun PreviewSpellGrid() {
    val spell = sampleSpell()
    SpellGrid(spell, spell.getColor(), spacingMedium)
}

@Composable
fun ApiSpell.getFormattedSchool() =
    stringResource(Res.string.formatted_spell_school_level, getSchool(), level)
