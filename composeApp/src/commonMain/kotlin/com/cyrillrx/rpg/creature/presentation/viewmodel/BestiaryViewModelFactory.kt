package com.cyrillrx.rpg.creature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.core.data.FileReader
import com.cyrillrx.rpg.creature.data.JsonBestiaryRepository
import kotlin.reflect.KClass

class BestiaryViewModelFactory(private val fileReader: FileReader) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return BestiaryViewModel(JsonBestiaryRepository(fileReader)) as T
    }
}
