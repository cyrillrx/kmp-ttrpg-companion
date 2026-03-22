package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.Purple300
import com.cyrillrx.rpg.core.presentation.theme.Purple400
import com.cyrillrx.rpg.core.presentation.theme.Purple500
import com.cyrillrx.rpg.core.presentation.theme.Purple600
import com.cyrillrx.rpg.core.presentation.theme.Purple700
import com.cyrillrx.rpg.core.presentation.theme.Purple800
import com.cyrillrx.rpg.core.presentation.theme.Purple900
import com.cyrillrx.rpg.core.presentation.theme.Scarlet
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.spell_level_1st
import rpg_companion.composeapp.generated.resources.spell_level_2nd
import rpg_companion.composeapp.generated.resources.spell_level_3rd
import rpg_companion.composeapp.generated.resources.spell_level_4th
import rpg_companion.composeapp.generated.resources.spell_level_5th
import rpg_companion.composeapp.generated.resources.spell_level_6th
import rpg_companion.composeapp.generated.resources.spell_level_7th
import rpg_companion.composeapp.generated.resources.spell_level_8th
import rpg_companion.composeapp.generated.resources.spell_level_9th
import rpg_companion.composeapp.generated.resources.spell_level_cantrip

@Composable
fun SpellListItem(
    spell: Spell,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val school = spell.schools.firstOrNull()
    val schoolColor = school.toColor()

    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(spacingCommon),
        ) {
            // School icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(schoolColor, RoundedCornerShape(12.dp)),
            ) {
                Icon(
                    imageVector = school.toIcon(),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(spacingCommon))

            // Spell info
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.weight(1f),
            ) {
                // Title
                Text(
                    text = spell.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // School + Casting time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = school.toIcon(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(spacingSmall))
                    Text(
                        text = school?.toFormattedString() ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = " · ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = spell.castingTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.width(spacingMedium))

            // Level
            Text(
                text = formattedLevel(spell.level),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun formattedLevel(level: Int): String = stringResource(
    when (level) {
        0 -> Res.string.spell_level_cantrip
        1 -> Res.string.spell_level_1st
        2 -> Res.string.spell_level_2nd
        3 -> Res.string.spell_level_3rd
        4 -> Res.string.spell_level_4th
        5 -> Res.string.spell_level_5th
        6 -> Res.string.spell_level_6th
        7 -> Res.string.spell_level_7th
        8 -> Res.string.spell_level_8th
        else -> Res.string.spell_level_9th
    },
)

private fun Spell.School?.toColor(): Color = when (this) {
    Spell.School.ABJURATION -> Purple400
    Spell.School.CONJURATION -> Purple700
    Spell.School.DIVINATION -> Purple500
    Spell.School.ENCHANTMENT -> Scarlet
    Spell.School.EVOCATION -> Purple600
    Spell.School.ILLUSION -> Purple800
    Spell.School.NECROMANCY -> Purple900
    Spell.School.TRANSMUTATION -> Purple300
    null -> Purple500
}

private fun Spell.School?.toIcon(): ImageVector = when (this) {
    Spell.School.ABJURATION -> Icons.Filled.Shield
    Spell.School.CONJURATION -> Icons.Filled.Flare
    Spell.School.DIVINATION -> Icons.Filled.Visibility
    Spell.School.ENCHANTMENT -> Icons.Filled.Psychology
    Spell.School.EVOCATION -> Icons.Filled.Bolt
    Spell.School.ILLUSION -> Icons.Filled.AutoAwesome
    Spell.School.NECROMANCY -> Icons.Outlined.Dangerous
    Spell.School.TRANSMUTATION -> Icons.Filled.SwapHoriz
    null -> Icons.Filled.AutoAwesome
}


@Preview
@Composable
private fun PreviewSpellListItemLight() {
    SpellListItemPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewSpellListItemDark() {
    SpellListItemPreview(darkTheme = true)
}

@Composable
private fun SpellListItemPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            SampleSpellRepository().getAll().forEach {
                SpellListItem(spell = it, onClick = {})
            }
        }
    }
}
