package com.cyrillrx.rpg.campaign.presentation

import com.cyrillrx.rpg.campaign.domain.Campaign

data class CampaignListState(
    val searchQuery: String = "",
    val body: Body,
) {
    sealed class Body {
        data object Loading : Body()

        data object Empty : Body()

        data class Error(val errorMessage: String) : Body()

        data class WithData(val searchResults: List<Campaign>) : Body()
    }
}
