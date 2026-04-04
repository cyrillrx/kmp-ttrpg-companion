package com.cyrillrx.rpg.creature.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.presentation.component.CreatureCompactListItem
import com.cyrillrx.rpg.userlist.presentation.ListItemProvider
import org.jetbrains.compose.resources.StringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.empty_list_browse_creatures

class CreatureItemProvider(
    private val onItemClicked: (String) -> Unit,
    override val onEmptyLayoutBtnClicked: () -> Unit = {},
) : ListItemProvider<Creature> {

    override val emptyLayoutIcon: ImageVector = Icons.Outlined.Pets
    override val emptyLayoutBtnText: StringResource = Res.string.empty_list_browse_creatures

    override fun getId(entity: Creature): String = entity.id

    override fun getDisplayName(entity: Creature): String = entity.name

    @Composable
    override fun ListItem(entity: Creature, modifier: Modifier) {
        CreatureCompactListItem(creature = entity, onClick = { onItemClicked(entity.id) }, modifier = modifier)
    }
}
