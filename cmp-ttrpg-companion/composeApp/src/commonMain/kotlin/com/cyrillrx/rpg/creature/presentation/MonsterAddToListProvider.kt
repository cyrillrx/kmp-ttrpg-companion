package com.cyrillrx.rpg.creature.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import com.cyrillrx.rpg.userlist.presentation.viewmodel.AddToListViewModelFactory
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_monsters

class MonsterAddToListProvider(
    repository: MonsterRepository,
    userListRepository: UserListRepository,
) : AddToListProvider<Monster> {
    override val listType: UserList.Type = UserList.Type.CREATURE

    override val viewModelFactory = AddToListViewModelFactory(
        listType = listType,
        userListRepository = userListRepository,
        entityRepository = repository,
        errorMessage = Res.string.error_while_loading_monsters,
    )

    @Composable
    override fun Header(entity: Monster) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = entity.resolveTranslation(currentLocale()).name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = entity.type.toFormattedString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            HorizontalDivider(modifier = Modifier.padding(spacingMedium))
        }
    }
}
