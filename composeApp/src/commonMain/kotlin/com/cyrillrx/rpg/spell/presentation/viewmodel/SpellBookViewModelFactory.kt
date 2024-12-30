package com.cyrillrx.rpg.spell.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.core.data.FileReader
import com.cyrillrx.rpg.spell.data.JsonSpellRepository
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRouter
import kotlin.reflect.KClass

class SpellBookViewModelFactory(
    private val router: SpellRouter,
    private val fileReader: FileReader,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return SpellBookViewModel(
            router = router,
            repository = JsonSpellRepository(fileReader),
        ) as T
    }
}
