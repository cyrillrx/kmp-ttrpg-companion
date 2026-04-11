package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.spell.data.SpellEntityRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellAddToListProvider
import com.cyrillrx.rpg.spell.presentation.SpellItemProvider
import com.cyrillrx.rpg.spell.presentation.component.SpellCardScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModelFactory
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import com.cyrillrx.rpg.userlist.presentation.component.ListDetailScreen
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModel
import com.cyrillrx.rpg.userlist.presentation.viewmodel.ListDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface SpellRoute {
    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val spellId: String) : NavKey

    @Serializable
    data class UserListDetail(val listId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerSpellRoutes() {
    subclass(SpellRoute.Compendium::class, SpellRoute.Compendium.serializer())
    subclass(SpellRoute.UserListDetail::class, SpellRoute.UserListDetail.serializer())
    subclass(SpellRoute.Detail::class, SpellRoute.Detail.serializer())
}

fun EntryProviderScope<NavKey>.handleSpellRoutes(
    router: SpellRouter,
    spellRepository: SpellRepository,
    userListRepository: UserListRepository,
) {
    entry<SpellRoute.Compendium> {
        val viewModelFactory = SpellListViewModelFactory(router, spellRepository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        val bottomSheetProvider = SpellAddToListProvider(
            spellRepository = spellRepository,
            userListRepository = userListRepository,
        )
        SpellListScreen(viewModel, router, bottomSheetProvider)
        // SpellCardCarouselScreen(viewModel, router)
    }

    entry<SpellRoute.Detail> { route ->
        val spellId = route.spellId
        val viewModelFactory = SpellDetailViewModelFactory(spellId, spellRepository)
        val viewModel = viewModel<SpellDetailViewModel>(key = spellId, factory = viewModelFactory)
        val bottomSheetProvider = SpellAddToListProvider(
            spellRepository = spellRepository,
            userListRepository = userListRepository,
        )
        SpellCardScreen(viewModel, router, bottomSheetProvider)
    }

    entry<SpellRoute.UserListDetail> { route ->
        val listId = route.listId
        val viewModelFactory = ListDetailViewModelFactory(
            listId = listId,
            userListRepository = userListRepository,
            repository = SpellEntityRepository(spellRepository),
        )
        val viewModel = viewModel<ListDetailViewModel<Spell>>(key = listId, factory = viewModelFactory)
        val itemProvider = SpellItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = router::openCompendium,
        )
        ListDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = router::navigateUp,
        )
    }
}
