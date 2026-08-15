package com.cyrillrx.rpg.spell.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.presentation.SpellAddToCollectionProvider
import com.cyrillrx.rpg.spell.presentation.SpellItemProvider
import com.cyrillrx.rpg.spell.presentation.component.SpellDetailScreen
import com.cyrillrx.rpg.spell.presentation.component.SpellListScreen
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellDetailViewModelFactory
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModel
import com.cyrillrx.rpg.spell.presentation.viewmodel.SpellListViewModelFactory
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import com.cyrillrx.rpg.usercollection.presentation.component.CollectionDetailScreen
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModel
import com.cyrillrx.rpg.usercollection.presentation.viewmodel.CollectionDetailViewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface SpellRoute {
    @Serializable
    data object Compendium : NavKey

    @Serializable
    data class Detail(val spellId: String) : NavKey

    @Serializable
    data class UserCollectionDetail(val collectionId: String) : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerSpellRoutes() {
    subclass(SpellRoute.Compendium::class, SpellRoute.Compendium.serializer())
    subclass(SpellRoute.UserCollectionDetail::class, SpellRoute.UserCollectionDetail.serializer())
    subclass(SpellRoute.Detail::class, SpellRoute.Detail.serializer())
}

fun EntryProviderScope<NavKey>.handleSpellRoutes(
    router: SpellRouter,
    spellRepository: SpellRepository,
    userCollectionRepository: UserCollectionRepository,
) {
    entry<SpellRoute.Compendium> {
        val viewModelFactory = SpellListViewModelFactory(spellRepository)
        val viewModel = viewModel<SpellListViewModel>(factory = viewModelFactory)
        val bottomSheetProvider = SpellAddToCollectionProvider(
            spellRepository = spellRepository,
            userCollectionRepository = userCollectionRepository,
        )
        SpellListScreen(viewModel, router, bottomSheetProvider)
    }

    entry<SpellRoute.Detail> { route ->
        val spellId = route.spellId
        val viewModelFactory = SpellDetailViewModelFactory(spellId, spellRepository)
        val viewModel = viewModel<SpellDetailViewModel>(key = spellId, factory = viewModelFactory)
        val bottomSheetProvider = SpellAddToCollectionProvider(
            spellRepository = spellRepository,
            userCollectionRepository = userCollectionRepository,
        )
        SpellDetailScreen(viewModel, router, bottomSheetProvider)
    }

    entry<SpellRoute.UserCollectionDetail> { route ->
        val collectionId = route.collectionId
        val viewModelFactory = CollectionDetailViewModelFactory(
            collectionId = collectionId,
            userCollectionRepository = userCollectionRepository,
            repository = spellRepository,
        )
        val viewModel = viewModel<CollectionDetailViewModel<Spell>>(key = collectionId, factory = viewModelFactory)
        val itemProvider = SpellItemProvider(
            onItemClicked = router::openDetail,
            onEmptyLayoutBtnClicked = router::openCompendium,
        )
        CollectionDetailScreen(
            viewModel = viewModel,
            itemProvider = itemProvider,
            onNavigateUp = router::navigateUp,
        )
    }
}
