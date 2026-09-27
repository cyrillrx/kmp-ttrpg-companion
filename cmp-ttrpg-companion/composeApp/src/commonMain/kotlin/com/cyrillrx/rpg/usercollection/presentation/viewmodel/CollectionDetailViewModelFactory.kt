package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.core.domain.Entity
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.settings.domain.UserPreferencesRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlin.reflect.KClass

class CollectionDetailViewModelFactory<T : Entity>(
    private val collectionId: String,
    private val userCollectionRepository: UserCollectionRepository,
    private val repository: EntityRepository<T>,
    private val prefsRepository: UserPreferencesRepository,
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: KClass<VM>, extras: CreationExtras): VM {
        @Suppress("UNCHECKED_CAST")
        return CollectionDetailViewModel(collectionId, userCollectionRepository, repository, prefsRepository) as VM
    }
}
