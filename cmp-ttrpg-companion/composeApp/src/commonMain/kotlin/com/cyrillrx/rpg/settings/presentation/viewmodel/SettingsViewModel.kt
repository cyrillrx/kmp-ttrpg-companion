package com.cyrillrx.rpg.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class SettingsViewModel(
    private val prefsRepository: UserPreferencesRepository,
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = prefsRepository.preferences

    fun setTheme(theme: Theme) {
        viewModelScope.launch { prefsRepository.setTheme(theme) }
    }
}

class SettingsViewModelFactory(
    private val prefsRepository: UserPreferencesRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        SettingsViewModel(prefsRepository) as T
}
