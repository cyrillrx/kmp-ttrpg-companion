package com.cyrillrx.rpg.campaign.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CampaignDetailScreen(
    campaign: Campaign,
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold { paddingValues ->
        Text(
            text = campaign.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable { onNavigateUpClicked() }
                .padding(paddingValues)
                .fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun PreviewCampaignDetailScreen() {
    val campaign = Campaign(
        id = "1",
        name = "Campaign name",
        ruleSet = RuleSet.DND5E,
    )
    AppThemePreview(darkTheme = false) {
        CampaignDetailScreen(campaign, {})
    }
}
