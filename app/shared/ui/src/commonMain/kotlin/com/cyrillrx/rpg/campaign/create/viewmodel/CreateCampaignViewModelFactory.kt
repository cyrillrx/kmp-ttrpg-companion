package com.cyrillrx.rpg.campaign.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import kotlin.reflect.KClass

class CreateCampaignViewModelFactory(
    private val repository: CampaignRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CreateCampaignViewModel(repository) as T
    }
}
