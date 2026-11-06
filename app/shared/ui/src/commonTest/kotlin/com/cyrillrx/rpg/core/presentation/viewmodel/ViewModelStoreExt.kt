package com.cyrillrx.rpg.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

/**
 * `ViewModel.clear()` is internal, so a test cannot cancel `viewModelScope` on its own: the view model has
 * to be held by a store, whose [ViewModelStore.clear] is the public way into that same call.
 */
fun <VM : ViewModel> ViewModelStore.hold(viewModelClass: KClass<VM>, build: () -> VM): VM =
    ViewModelProvider.create(
        this,
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T = build() as T
        },
    )[viewModelClass]
