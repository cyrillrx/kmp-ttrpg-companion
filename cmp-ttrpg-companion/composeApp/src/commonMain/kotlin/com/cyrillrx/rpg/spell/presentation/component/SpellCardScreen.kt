package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.AddToListBottomSheet
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.userlist.data.RamUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.error_spell_not_found

@Composable
fun SpellCardScreen(
    viewModel: SpellDetailViewModel,
    userListRepository: UserListRepository,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_spell_not_found, s.id))
        is DetailState.Found -> SpellCardScreen(s.item, userListRepository, onNavigateUpClicked)
    }
}

@Composable
fun SpellCardScreen(
    spell: Spell,
    userListRepository: UserListRepository,
    onNavigateUpClicked: () -> Unit,
) {
    var showAddToList by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SpellCard(
                spell = spell,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateUpClicked() },
            )
            Button(
                onClick = { showAddToList = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium),
            ) {
                Text(stringResource(Res.string.btn_add_to_list))
            }
        }
    }

    if (showAddToList) {
        AddToListBottomSheet(
            itemId = spell.id,
            listType = UserList.Type.SPELL,
            userListRepository = userListRepository,
            onDismiss = { showAddToList = false },
        )
    }
}

@Preview
@Composable
fun PreviewSpellCardScreen() {
    val spell = SampleSpellRepository.getFirst()
    SpellCardScreen(spell, RamUserListRepository(), {})
}
