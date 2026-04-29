package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import kotlin.reflect.KClass

class MonsterDetailViewModelFactory(
    private val monsterId: String,
    private val repository: MonsterRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return MonsterDetailViewModel(monsterId, repository) as T
    }
}
