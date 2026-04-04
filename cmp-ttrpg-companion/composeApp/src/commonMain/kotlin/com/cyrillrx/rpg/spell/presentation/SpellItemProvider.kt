package com.cyrillrx.rpg.spell.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.component.SpellListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.empty_list_browse_spells
import rpg_companion.composeapp.generated.resources.message_list_is_empty

class SpellItemProvider(
    private val onItemClicked: (String) -> Unit,
    private val onAddItemsClicked: () -> Unit = {},
) : ListItemProvider<Spell> {

    override fun getId(entity: Spell): String = entity.id

    override fun getDisplayName(entity: Spell): String = entity.title

    @Composable
    override fun ListItem(entity: Spell, modifier: Modifier) {
        SpellListItem(spell = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }

    @Composable
    override fun EmptyLayout() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.message_list_is_empty),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddItemsClicked) {
                Text(text = stringResource(Res.string.empty_list_browse_spells))
            }
        }
    }
}
