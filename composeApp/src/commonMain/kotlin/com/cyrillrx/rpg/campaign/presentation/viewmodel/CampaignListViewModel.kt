package com.cyrillrx.rpg.campaign.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.presentation.CampaignListState
import com.cyrillrx.rpg.campaign.presentation.navigation.CampaignRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CampaignListViewModel(
    private val router: CampaignRouter,
    private val repository: CampaignRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    private val _state: MutableStateFlow<CampaignListState> = MutableStateFlow(
        CampaignListState(searchQuery = "", body = CampaignListState.Body.Empty),
    )
    val state: StateFlow<CampaignListState> = _state.asStateFlow()

    init {
        onSearchQueryChanged(query = "")
    }

    fun onSearchQueryChanged(query: String) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch { updateData(query) }
    }

    fun onCampaignClicked(campaign: Campaign) {
        router.openCampaignDetail(campaign)
    }

    fun onCreateCampaignClicked() {
        router.openCreateCampaign()
    }

    private suspend fun updateData(query: String) {
        _state.update { CampaignListState(searchQuery = query, body = CampaignListState.Body.Loading) }

        try {
            val campaigns = if (query.isBlank()) repository.getAll() else repository.filter(query)
            if (campaigns.isEmpty()) {
                _state.update { _state.value.copy(body = CampaignListState.Body.Empty) }
            } else {
                _state.update { _state.value.copy(body = CampaignListState.Body.WithData(campaigns))}
            }
        } catch (e: Exception) {
            // TODO translate
            _state.update { _state.value.copy(body = CampaignListState.Body.Error(errorMessage = "Error while loading")) }
        }
    }
}
