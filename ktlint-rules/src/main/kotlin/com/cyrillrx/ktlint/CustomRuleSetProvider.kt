package com.cyrillrx.ktlint

import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId

const val RULE_SET_ID = "cyrillrx"

class CustomRuleSetProvider : RuleSetProviderV3(RuleSetId(RULE_SET_ID)) {
    override fun getRuleProviders(): Set<RuleProvider> =
        setOf(
            RuleProvider { BlankLineAfterBlockRule() },
        )
}
