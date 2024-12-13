package com.cyrillrx.rpg.magicalitems.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.core.data.FileReader
import com.cyrillrx.rpg.magicalitems.data.JsonMagicalItemRepository
import kotlin.reflect.KClass

class InventoryViewModelFactory(private val fileReader: FileReader) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return InventoryViewModel(JsonMagicalItemRepository(fileReader)) as T
    }
}
