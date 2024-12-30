package com.cyrillrx.rpg.campaign.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.campaign.data.RamCampaignRepository
import com.cyrillrx.rpg.campaign.presentation.navigation.CampaignRouter
import kotlin.reflect.KClass

class CampaignListViewModelFactory(private val router: CampaignRouter) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CampaignListViewModel(router, RamCampaignRepository()) as T
    }
}
