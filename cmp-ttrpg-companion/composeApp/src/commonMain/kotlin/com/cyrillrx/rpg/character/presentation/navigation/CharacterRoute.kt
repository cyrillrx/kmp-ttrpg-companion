package com.cyrillrx.rpg.character.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.character.domain.PlayerCharacterRepository
import com.cyrillrx.rpg.character.presentation.component.CreatePlayerCharacterScreen
import com.cyrillrx.rpg.character.presentation.component.PlayerCharacterDetailScreen
import com.cyrillrx.rpg.character.presentation.component.PlayerCharacterListScreen
import com.cyrillrx.rpg.character.presentation.viewmodel.PlayerCharacterListViewModel
import com.cyrillrx.rpg.character.presentation.viewmodel.PlayerCharacterListViewModelFactory
import com.cyrillrx.rpg.core.navigation.navigateUp
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface PlayerCharacterRoute {
    @Serializable
    data object List : NavKey

    @Serializable
    data class Detail(val serializedPlayerCharacter: String) : NavKey

    @Serializable
    data object Create : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerCharacterRoutes() {
    subclass(PlayerCharacterRoute.List::class, PlayerCharacterRoute.List.serializer())
    subclass(PlayerCharacterRoute.Detail::class, PlayerCharacterRoute.Detail.serializer())
    subclass(PlayerCharacterRoute.Create::class, PlayerCharacterRoute.Create.serializer())
}

fun EntryProviderScope<NavKey>.handlePlayerCharacterRoutes(
    backStack: NavBackStack<NavKey>,
    repository: PlayerCharacterRepository,
) {
    entry<PlayerCharacterRoute.List> {
        val router = PlayerCharacterRouterImpl(backStack)
        val viewModelFactory = PlayerCharacterListViewModelFactory(router, repository)
        val viewModel = viewModel<PlayerCharacterListViewModel>(factory = viewModelFactory)
        PlayerCharacterListScreen(viewModel, router)
    }

    entry<PlayerCharacterRoute.Detail> { route ->
        PlayerCharacterDetailScreen(
            character = route.serializedPlayerCharacter.deserialize(),
            onNavigateUpClicked = backStack::navigateUp,
        )
    }

    entry<PlayerCharacterRoute.Create> {
        CreatePlayerCharacterScreen(
            onNavigateUpClicked = backStack::navigateUp,
        )
    }
}
