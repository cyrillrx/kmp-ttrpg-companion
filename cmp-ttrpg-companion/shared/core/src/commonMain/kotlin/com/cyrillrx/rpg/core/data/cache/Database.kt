package com.cyrillrx.rpg.core.data.cache

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.userlist.domain.UserList
import kotlinx.datetime.Instant

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllCharacters(): List<Character> =
        dbQuery.selectAllCharacters(::mapCharacterSelecting).executeAsList()

    fun getCharacter(id: String): Character? =
        dbQuery.selectCharacterById(id, ::mapCharacterSelecting).executeAsOneOrNull()

    fun saveCharacter(character: Character) {
        dbQuery.saveCharacter(id = character.id, data_ = character.serialize())
    }

    fun getCharactersByIds(ids: Collection<String>): List<Character> =
        dbQuery.selectCharactersByIds(ids, ::mapCharacterSelecting).executeAsList()

    fun deleteCharacter(id: String) {
        dbQuery.deleteCharacter(id)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mapCharacterSelecting(id: String, data: String): Character = data.deserialize()

    fun getAllCampaigns(): List<Campaign> {
        return dbQuery.selectAllCampaigns(::mapCampaignSelecting).executeAsList()
    }

    fun insertCampaign(campaign: Campaign) {
        dbQuery.insertCampaign(
            id = campaign.id,
            name = campaign.name,
            ruleSet = campaign.ruleSet.ordinal.toLong(),
        )
    }

    fun getCampaign(id: String): Campaign? {
        return dbQuery.selectCampaignById(id, ::mapCampaignSelecting).executeAsOneOrNull()
    }

    fun deleteCampaign(id: String) {
        dbQuery.deleteCampaign(id)
    }

    private fun mapCampaignSelecting(id: String, name: String, ruleSet: Long): Campaign =
        Campaign(id = id, name = name, ruleSet = RuleSet.fromInt(ruleSet.toInt()))

    fun getAllUserLists(type: UserList.Type): List<UserList> =
        dbQuery.selectAllUserListsByType(type.name, ::mapUserListSelecting).executeAsList()

    fun getUserList(id: String): UserList? =
        dbQuery.selectUserListById(id, ::mapUserListSelecting).executeAsOneOrNull()

    fun saveUserList(list: UserList) {
        dbQuery.saveUserList(
            id = list.id,
            name = list.name,
            type = list.type.name,
            itemIds = list.itemIds.joinToString(LIST_DELIMITER),
            lastModified = list.lastModified.toEpochMilliseconds(),
        )
    }

    fun deleteUserList(id: String) {
        dbQuery.deleteUserList(id)
    }

    fun initUserPreferences() {
        dbQuery.initUserPreferences()
    }

    fun getUserPreferences(): UserPreferences =
        dbQuery.getUserPreferences { _, theme, distanceUnit ->
            UserPreferences(
                theme = Theme.entries.find { it.name.equals(theme, ignoreCase = true) }
                    ?: Theme.SYSTEM,
                distanceUnit = DistanceUnit.entries.find { it.name.equals(distanceUnit, ignoreCase = true) }
                    ?: DistanceUnit.FEET,
            )
        }.executeAsOneOrNull() ?: UserPreferences()

    fun updateTheme(theme: Theme) {
        dbQuery.updateTheme(theme.name.lowercase())
    }

    fun updateDistanceUnit(distanceUnit: DistanceUnit) {
        dbQuery.updateDistanceUnit(distanceUnit.name.lowercase())
    }

    private fun mapUserListSelecting(id: String, name: String, type: String, itemIds: String, lastModified: Long) =
        UserList(
            id = id,
            name = name,
            type = UserList.Type.valueOf(type),
            itemIds = if (itemIds.isEmpty()) emptyList() else itemIds.split(LIST_DELIMITER),
            lastModified = Instant.fromEpochMilliseconds(lastModified),
        )

    companion object {
        const val DATABASE_NAME = "ttrpg_companion.db"
        const val LIST_DELIMITER = "\u001F"
    }
}
