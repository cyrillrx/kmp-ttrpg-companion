package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import kotlin.reflect.KClass

class DiceHistoryViewModelFactory(
    private val historyRepository: RollHistoryRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        DiceHistoryViewModel(historyRepository) as T
}
