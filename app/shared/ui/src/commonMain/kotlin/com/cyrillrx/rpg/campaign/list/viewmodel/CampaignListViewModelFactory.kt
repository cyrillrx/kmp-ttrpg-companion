package com.cyrillrx.rpg.campaign.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import kotlin.reflect.KClass

class CampaignListViewModelFactory(
    private val repository: CampaignRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return CampaignListViewModel(repository) as T
    }
}
