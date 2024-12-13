package com.cyrillrx.rpg.spellbook.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.core.presentation.componenent.BookmarkButton
import com.cyrillrx.rpg.core.presentation.componenent.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spellbook.domain.Spell
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.formatted_spell_casting_time
import rpg_companion.composeapp.generated.resources.formatted_spell_components
import rpg_companion.composeapp.generated.resources.formatted_spell_duration
import rpg_companion.composeapp.generated.resources.formatted_spell_range

@Composable
fun SpellListItem(
    modifier: Modifier,
    spell: Spell,
    isSaved: Boolean,
    onSaveClicked: (Spell) -> Unit,
) {
    Card(
        shape = CutCornerShape(0.dp),
        modifier = modifier.padding(spacingSmall),
    ) {
        Column {
            Header(spell, isSaved, onSaveClicked)

            SpellSpecs(
                spell,
                Modifier.padding(
                    horizontal = spacingCommon,
                    vertical = spacingSmall,
                ),
            )

            HtmlText(
                text = spell.description,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                modifier = Modifier
                    .padding(spacingMedium)
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun Header(spell: Spell, isSaved: Boolean, onSaveClicked: (Spell) -> Unit) {
    Row(
        modifier = Modifier.background(spell.getColor()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = spell.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = spacingMedium,
                        end = spacingMedium,
                        top = spacingMedium / 2,
                    ),
            )
            Text(
                text = spell.getFormattedSchool(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = spacingMedium,
                        end = spacingMedium,
                        bottom = spacingMedium / 2,
                    ),
            )
        }

        BookmarkButton(
            checked = isSaved,
            modifier = Modifier
                .clickable { onSaveClicked(spell) }
                .padding(spacingCommon),
        )
    }
}

@Composable
private fun SpellSpecs(spell: Spell, modifier: Modifier) {
    Column(modifier) {
        Row {
            Text(
                text = stringResource(Res.string.formatted_spell_casting_time),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.castingTime, modifier = Modifier.padding(start = 4.dp))
        }
        Row {
            Text(
                text = stringResource(Res.string.formatted_spell_components),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.components, modifier = Modifier.padding(start = 4.dp))
        }
        Row {
            Text(
                text = stringResource(Res.string.formatted_spell_range),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.range, modifier = Modifier.padding(start = 4.dp))
        }
        Row {
            Text(
                text = stringResource(Res.string.formatted_spell_duration),
                fontWeight = FontWeight.Bold,
            )
            Text(text = spell.duration, modifier = Modifier.padding(start = 4.dp))
        }
    }
}
