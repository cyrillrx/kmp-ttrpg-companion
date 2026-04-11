package com.cyrillrx.rpg.spell.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.core.presentation.component.dnd.toIcon
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SpellEntityRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModelFactory
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_spells

class SpellAddToListProvider(
    spellRepository: SpellRepository,
    userListRepository: UserListRepository,
) : AddToListProvider<Spell> {
    override val listType: UserList.Type = UserList.Type.SPELL

    override val viewModelFactory = AddToListViewModelFactory(
        listType = listType,
        userListRepository = userListRepository,
        entityRepository = SpellEntityRepository(spellRepository),
        errorMessage = Res.string.error_while_loading_spells,
    )

    @Composable
    override fun Header(entity: Spell) {
        val school = entity.schools.firstOrNull()
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = entity.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
            ) {
                Icon(
                    imageVector = school.toIcon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(iconSizeMedium),
                )
                Text(
                    text = entity.getSubtitle(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            HorizontalDivider(modifier = Modifier.padding(spacingMedium))
        }
    }
}
