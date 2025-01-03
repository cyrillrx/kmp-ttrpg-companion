package com.cyrillrx.rpg.campaign.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.presentation.navigation.CampaignRouter
import kotlin.reflect.KClass

class CreateCampaignViewModelFactory(
    private val router: CampaignRouter,
    private val repository: CampaignRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CreateCampaignViewModel(router, repository) as T
    }
}
