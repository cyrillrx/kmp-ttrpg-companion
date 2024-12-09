package com.cyrillrx.rpg.spellbook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.core.data.FileReader
import com.cyrillrx.rpg.spellbook.data.JsonSpellRepository
import kotlin.reflect.KClass

class SpellBookViewModelFactory(private val fileReader: FileReader) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return SpellBookViewModel(JsonSpellRepository(fileReader)) as T
    }
}