package com.cyrillrx.rpg.campaign.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.campaign.domain.Campaign

@Composable
fun CampaignDetailScreen(campaign: Campaign) {
    Text(
        text = campaign.name,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxSize(),
    )
}
