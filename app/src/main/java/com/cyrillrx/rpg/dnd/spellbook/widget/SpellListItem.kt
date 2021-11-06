package com.cyrillrx.rpg.dnd.spellbook.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.spellbook.Spell
import com.cyrillrx.rpg.ui.theme.spacingCommon
import com.cyrillrx.rpg.ui.theme.spacingSmall
import com.cyrillrx.rpg.ui.widget.BookmarkButton

@Composable
fun SpellListItem(
    modifier: Modifier,
    spell: Spell,
    savedSpells: List<Spell>,
    onSaveClicked: (Spell) -> Unit,
) {
    Card(
        shape = CutCornerShape(0.dp),
        modifier = modifier.padding(spacingSmall),
    ) {
        Column {
            Header(spell, savedSpells, onSaveClicked)

            SpellSpecs(
                spell,
                Modifier.padding(
                    horizontal = spacingCommon,
                    vertical = spacingSmall,
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

@Composable
private fun Header(spell: Spell, savedSpells: List<Spell>, onSaveClicked: (Spell) -> Unit) {
    Row(
        modifier = Modifier.background(spell.getColor()),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(Modifier.weight(1f)) {
            Text(
                text = spell.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
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
                    .padding(
                        start = textPadding,
                        end = textPadding,
                        bottom = textPadding / 2,
                    ),
            )
        }

        var checked by remember { mutableStateOf(spell in savedSpells) }
        BookmarkButton(
            checked = checked,
            modifier = Modifier
                .clickable {
                    onSaveClicked(spell)
                    checked = spell in savedSpells
                }
                .padding(spacingCommon),
        )
    }
}

@Composable
private fun SpellSpecs(spell: Spell, modifier: Modifier) {
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
