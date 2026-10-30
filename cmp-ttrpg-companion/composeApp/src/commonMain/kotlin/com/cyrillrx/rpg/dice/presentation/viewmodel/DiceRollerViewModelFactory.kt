package com.cyrillrx.rpg.dice.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.dice.domain.DiceRollerDraftRepository
import kotlin.reflect.KClass

class DiceRollerViewModelFactory(
    private val draftRepository: DiceRollerDraftRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        DiceRollerViewModel(draftRepository) as T
}
