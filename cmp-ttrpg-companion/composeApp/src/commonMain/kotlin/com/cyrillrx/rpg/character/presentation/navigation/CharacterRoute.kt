package com.cyrillrx.rpg.character.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.component.CreateCharacterScreen
import com.cyrillrx.rpg.character.presentation.component.CharacterDetailScreen
import com.cyrillrx.rpg.character.presentation.component.CharacterListScreen
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterListViewModel
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterListViewModelFactory
import com.cyrillrx.rpg.core.navigation.navigateUp
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface CharacterRoute {
    @Serializable
    data object List : NavKey

    @Serializable
    data class Detail(val serializedCharacter: String) : NavKey

    @Serializable
    data object Create : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerCharacterRoutes() {
    subclass(CharacterRoute.List::class, CharacterRoute.List.serializer())
    subclass(CharacterRoute.Detail::class, CharacterRoute.Detail.serializer())
    subclass(CharacterRoute.Create::class, CharacterRoute.Create.serializer())
}

fun EntryProviderScope<NavKey>.handleCharacterRoutes(
    backStack: NavBackStack<NavKey>,
    repository: CharacterRepository,
) {
    entry<CharacterRoute.List> {
        val router = CharacterRouterImpl(backStack)
        val viewModelFactory = CharacterListViewModelFactory(router, repository)
        val viewModel = viewModel<CharacterListViewModel>(factory = viewModelFactory)
        CharacterListScreen(viewModel, router)
    }

    entry<CharacterRoute.Detail> { route ->
        CharacterDetailScreen(
            character = route.serializedCharacter.deserialize(),
            onNavigateUpClicked = backStack::navigateUp,
        )
    }

    entry<CharacterRoute.Create> {
        CreateCharacterScreen(
            onNavigateUpClicked = backStack::navigateUp,
        )
    }
}
