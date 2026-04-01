package com.cyrillrx.rpg.campaign.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignFilter
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.list.CampaignListState
import com.cyrillrx.rpg.campaign.navigation.CampaignRouter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_campaign
import kotlin.coroutines.cancellation.CancellationException

class CampaignListViewModel(
    private val router: CampaignRouter,
    private val repository: CampaignRepository,
) : ViewModel() {

    private var updateJob: Job? = null
    val state: StateFlow<CampaignListState>
        field = MutableStateFlow(CampaignListState(searchQuery = "", body = CampaignListState.Body.Empty))

    fun onNavigateUpClicked() {
        router.navigateUp()
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
        state.update { CampaignListState(searchQuery = query, body = CampaignListState.Body.Loading) }

        try {
            val filter = CampaignFilter(query = query)
            val campaigns = repository.getAll(filter)
            if (campaigns.isEmpty()) {
                state.update { it.copy(body = CampaignListState.Body.Empty) }
            } else {
                state.update { it.copy(body = CampaignListState.Body.WithData(campaigns)) }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            state.update { it.copy(body = CampaignListState.Body.Error(errorMessage = Res.string.error_while_loading_campaign)) }
        }
    }
}
