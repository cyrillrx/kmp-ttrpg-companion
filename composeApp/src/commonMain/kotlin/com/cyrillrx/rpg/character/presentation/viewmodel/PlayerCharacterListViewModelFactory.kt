package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.character.domain.PlayerCharacterRepository
import com.cyrillrx.rpg.character.presentation.navigation.PlayerCharacterRouter
import kotlin.reflect.KClass

class PlayerCharacterListViewModelFactory(
    private val router: PlayerCharacterRouter,
    private val repository: PlayerCharacterRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return PlayerCharacterListViewModel(router, repository) as T
    }
}
