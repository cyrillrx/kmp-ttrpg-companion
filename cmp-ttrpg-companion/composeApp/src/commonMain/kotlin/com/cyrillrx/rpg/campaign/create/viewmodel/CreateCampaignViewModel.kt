package com.cyrillrx.rpg.campaign.create.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.campaign.create.CreateCampaignState
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.CampaignRepository
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.campaign.navigation.CampaignRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateCampaignViewModel(
    private val router: CampaignRouter,
    private val repository: CampaignRepository,
) : ViewModel() {

    val state: StateFlow<CreateCampaignState>
        field = MutableStateFlow(CreateCampaignState(campaignName = "", selectedRuleSet = RuleSet.UNDEFINED, error = null))

    fun onNavigateUpClicked() {
        router.navigateUp()
    }

    fun onCampaignNameChanged(name: String) {
        state.update { state.value.copy(campaignName = name) }
    }

    fun onRuleSetSelected(ruleSet: RuleSet) {
        state.update { state.value.copy(selectedRuleSet = ruleSet) }
    }

    fun onCreateCampaignClicked() {
        val currentState = state.value
        val campaignName = currentState.campaignName.trim()
        val selectedRuleSet = currentState.selectedRuleSet

        if (campaignName.isBlank()) {
            state.update { state.value.copy(error = CreateCampaignError.EmptyCampaignName) }
            return
        }

        if (selectedRuleSet == RuleSet.UNDEFINED) {
            state.update { state.value.copy(error = CreateCampaignError.UndefinedRuleSet) }
            return
        }

        val newCampaign = Campaign(
            id = campaignName,
            name = campaignName,
            ruleSet = selectedRuleSet,
        )

        viewModelScope.launch {
            val campaignAlreadyExists = repository.get(newCampaign.id) != null
            if (campaignAlreadyExists) {
                state.update { state.value.copy(error = CreateCampaignError.CampaignAlreadyExists) }
                return@launch
            }

            repository.save(newCampaign)
            onNavigateUpClicked()
        }
    }

    fun clearError() {
        state.update { state.value.copy(error = null) }
    }
}
