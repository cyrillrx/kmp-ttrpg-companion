package com.cyrillrx.rpg.server.campaign

import com.cyrillrx.rpg.server.campaign.domain.RuleSet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

/**
 * Test class for CreateCampaignRequest.
 *
 * This class provides unit tests for the getRuleSetEnum() method in CreateCampaignRequest.
 * The method is responsible for converting the string representation of a rule set to its corresponding RuleSet enum.
 */
class CreateCampaignRequestTest {

    @Test
    fun `getRuleSetEnum should return the correct RuleSet enum when a valid ruleSet is provided`() {
        // Arrange
        val request = CreateCampaignRequest(name = "Example Campaign", ruleSet = "DND5E")

        // Act
        val result = request.getRuleSetEnum()

        // Assert
        assertEquals(RuleSet.DND5E, result)
    }

    @Test
    fun `getRuleSetEnum should handle case sensitivity correctly and throw IllegalArgumentException`() {
        // Arrange
        val request = CreateCampaignRequest(name = "Example Campaign", ruleSet = "dnd5e")

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            request.getRuleSetEnum()
        }
    }

    @Test
    fun `getRuleSetEnum should throw IllegalArgumentException when an invalid ruleSet is provided`() {
        // Arrange
        val request = CreateCampaignRequest(name = "Example Campaign", ruleSet = "INVALID_RULESET")

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            request.getRuleSetEnum()
        }
    }

    @Test
    fun `getRuleSetEnum should return OTHER when ruleSet is OTHER`() {
        // Arrange
        val request = CreateCampaignRequest(name = "Example Campaign", ruleSet = "OTHER")

        // Act
        val result = request.getRuleSetEnum()

        // Assert
        assertEquals(RuleSet.OTHER, result)
    }

    @Test
    fun `getRuleSetEnum should return UNDEFINED when ruleSet is UNDEFINED`() {
        // Arrange
        val request = CreateCampaignRequest(name = "Example Campaign", ruleSet = "UNDEFINED")

        // Act
        val result = request.getRuleSetEnum()

        // Assert
        assertEquals(RuleSet.UNDEFINED, result)
    }
}