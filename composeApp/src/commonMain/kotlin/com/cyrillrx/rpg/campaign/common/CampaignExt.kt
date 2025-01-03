package com.cyrillrx.rpg.campaign.common

import com.cyrillrx.rpg.campaign.create.viewmodel.CreateCampaignError
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.class_cleric
import rpg_companion.composeapp.generated.resources.error_campaign_already_exists
import rpg_companion.composeapp.generated.resources.error_empty_campaign_name
import rpg_companion.composeapp.generated.resources.error_undefined_rule_set
import rpg_companion.composeapp.generated.resources.ruleset_call_of_cthulhu_7e
import rpg_companion.composeapp.generated.resources.ruleset_dnd5e
import rpg_companion.composeapp.generated.resources.ruleset_other
import rpg_companion.composeapp.generated.resources.ruleset_pathfinder_2e
import rpg_companion.composeapp.generated.resources.ruleset_starfinder
import rpg_companion.composeapp.generated.resources.ruleset_undefined
import rpg_companion.composeapp.generated.resources.ruleset_vampire_the_masquerade_5e

fun Campaign.getIcon(): DrawableResource = when (ruleSet) {
    RuleSet.DND5E -> Res.drawable.class_cleric
    RuleSet.PATHFINDER_2E -> Res.drawable.class_cleric
    RuleSet.STARFINDER -> Res.drawable.class_cleric
    RuleSet.CALL_OF_CTHULHU_7E -> Res.drawable.class_cleric
    RuleSet.VAMPIRE_THE_MASQUERADE_5E -> Res.drawable.class_cleric
    RuleSet.OTHER -> Res.drawable.class_cleric
    RuleSet.UNDEFINED -> Res.drawable.class_cleric
}

fun RuleSet.getName(): StringResource = when (this) {
    RuleSet.DND5E -> Res.string.ruleset_dnd5e
    RuleSet.PATHFINDER_2E -> Res.string.ruleset_pathfinder_2e
    RuleSet.STARFINDER -> Res.string.ruleset_starfinder
    RuleSet.CALL_OF_CTHULHU_7E -> Res.string.ruleset_call_of_cthulhu_7e
    RuleSet.VAMPIRE_THE_MASQUERADE_5E -> Res.string.ruleset_vampire_the_masquerade_5e
    RuleSet.OTHER -> Res.string.ruleset_other
    RuleSet.UNDEFINED -> Res.string.ruleset_undefined
}

fun CreateCampaignError.getMessage(): StringResource = when (this) {
    CreateCampaignError.EmptyCampaignName -> Res.string.error_empty_campaign_name
    CreateCampaignError.UndefinedRuleSet -> Res.string.error_undefined_rule_set
    CreateCampaignError.CampaignAlreadyExists -> Res.string.error_campaign_already_exists
}
