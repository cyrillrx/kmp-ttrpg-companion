package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.presentation.navigation.MonsterRouter
import kotlin.reflect.KClass

class MonsterListViewModelFactory(
    private val router: MonsterRouter,
    private val repository: MonsterRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return MonsterListViewModel(router, repository) as T
    }
}
