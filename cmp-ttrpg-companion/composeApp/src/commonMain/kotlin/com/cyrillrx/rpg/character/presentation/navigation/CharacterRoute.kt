package com.cyrillrx.rpg.character.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.component.CharacterDetailScreen
import com.cyrillrx.rpg.character.presentation.component.CharacterListScreen
import com.cyrillrx.rpg.character.presentation.component.CharacterPresetGalleryScreen
import com.cyrillrx.rpg.character.presentation.component.CreateCharacterScreen
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterEditViewModel
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterEditViewModelFactory
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterListViewModel
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterListViewModelFactory
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterPresetGalleryViewModel
import com.cyrillrx.rpg.character.presentation.viewmodel.CharacterPresetGalleryViewModelFactory
import com.cyrillrx.rpg.core.navigation.navigateUp
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder

interface CharacterRoute {
    @Serializable
    data object List : NavKey

    @Serializable
    data class Detail(val characterId: String) : NavKey

    @Serializable
    data object Create : NavKey

    @Serializable
    data object PresetGallery : NavKey
}

fun PolymorphicModuleBuilder<NavKey>.registerCharacterRoutes() {
    subclass(CharacterRoute.List::class, CharacterRoute.List.serializer())
    subclass(CharacterRoute.Detail::class, CharacterRoute.Detail.serializer())
    subclass(CharacterRoute.Create::class, CharacterRoute.Create.serializer())
    subclass(CharacterRoute.PresetGallery::class, CharacterRoute.PresetGallery.serializer())
}

fun EntryProviderScope<NavKey>.handleCharacterRoutes(
    backStack: NavBackStack<NavKey>,
    characterRepository: CharacterRepository,
    pcPresetRepository: CharacterRepository,
    npcPresetRepository: CharacterRepository,
) {
    entry<CharacterRoute.List> {
        val router = CharacterRouterImpl(backStack)
        val viewModelFactory = CharacterListViewModelFactory(characterRepository)
        val viewModel = viewModel<CharacterListViewModel>(factory = viewModelFactory)
        CharacterListScreen(viewModel, router)
    }

    entry<CharacterRoute.Detail> { route ->
        val router = CharacterRouterImpl(backStack)
        val factory = CharacterEditViewModelFactory(route.characterId, characterRepository)
        val viewModel = viewModel<CharacterEditViewModel>(key = route.characterId, factory = factory)
        CharacterDetailScreen(viewModel, router)
    }

    entry<CharacterRoute.Create> {
        CreateCharacterScreen(
            onNavigateUpClicked = backStack::navigateUp,
        )
    }

    entry<CharacterRoute.PresetGallery> {
        val router = CharacterRouterImpl(backStack)
        val viewModelFactory = CharacterPresetGalleryViewModelFactory(pcPresetRepository, npcPresetRepository, characterRepository)
        val viewModel = viewModel<CharacterPresetGalleryViewModel>(factory = viewModelFactory)
        CharacterPresetGalleryScreen(viewModel, router)
    }
}
