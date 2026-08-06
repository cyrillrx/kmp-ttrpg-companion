package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

class AddToCollectionViewModelFactory<T>(
    private val listType: UserCollection.Type,
    private val userCollectionRepository: UserCollectionRepository,
    private val entityRepository: EntityRepository<T>,
    private val errorMessage: StringResource,
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: KClass<VM>, extras: CreationExtras): VM {
        @Suppress("UNCHECKED_CAST")
        return AddToCollectionViewModel(listType, userCollectionRepository, entityRepository, errorMessage) as VM
    }
}
