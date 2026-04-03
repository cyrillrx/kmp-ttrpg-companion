package com.cyrillrx.rpg.creature.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider

class CreatureItemProvider(
    private val onItemClicked: (String) -> Unit,
) : ListItemProvider<Creature> {

    override fun getId(entity: Creature): String = entity.id

    override fun getDisplayName(entity: Creature): String = entity.name

    @Composable
    override fun ListItem(entity: Creature, modifier: Modifier) {
        CreatureCompactListItem(creature = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }
}
