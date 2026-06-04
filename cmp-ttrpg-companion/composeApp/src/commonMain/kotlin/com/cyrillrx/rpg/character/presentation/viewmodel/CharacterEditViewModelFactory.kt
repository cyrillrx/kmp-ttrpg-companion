package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.character.domain.CharacterRepository
import kotlin.reflect.KClass

class CharacterEditViewModelFactory(
    private val characterId: String,
    private val characterRepository: CharacterRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        CharacterEditViewModel(characterId, characterRepository) as T
}
