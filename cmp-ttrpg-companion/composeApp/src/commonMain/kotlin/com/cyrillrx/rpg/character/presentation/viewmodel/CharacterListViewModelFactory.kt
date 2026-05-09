package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import kotlin.reflect.KClass

class CharacterListViewModelFactory(
    private val router: CharacterRouter,
    private val repository: CharacterRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CharacterListViewModel(router, repository) as T
    }
}
