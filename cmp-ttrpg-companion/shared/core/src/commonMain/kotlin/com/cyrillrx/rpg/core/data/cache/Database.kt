package com.cyrillrx.rpg.core.data.cache

import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.data.serialize
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.settings.domain.DistanceUnit
import com.cyrillrx.rpg.settings.domain.Palette
import com.cyrillrx.rpg.settings.domain.Theme
import com.cyrillrx.rpg.settings.domain.UserPreferences
import com.cyrillrx.rpg.userlist.domain.UserList
import kotlin.time.Instant

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun getAllCharacters(): List<Stored<Character>> =
        dbQuery.selectAllCharacters(::mapCharacterStored).executeAsList()

    fun getCharacter(id: String): Character? =
        dbQuery.selectCharacterById(id, ::mapCharacterSelecting).executeAsOneOrNull()

    fun saveCharacter(character: Character, updatedAt: Long) {
        dbQuery.saveCharacter(
            id = character.id,
            data_ = character.serialize(),
            updatedAt = updatedAt,
        )
    }

    fun getCharactersByIds(ids: Collection<String>): List<Character> =
        dbQuery.selectCharactersByIds(ids, ::mapCharacterSelecting).executeAsList()

    fun deleteCharacter(id: String) {
        dbQuery.deleteCharacter(id)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mapCharacterSelecting(id: String, data: String, updatedAt: Long): Character =
        data.deserialize()

    private fun mapCharacterStored(id: String, data: String, updatedAt: Long): Stored<Character> =
        Stored(
            value = data.deserialize(),
            updatedAt = Instant.fromEpochMilliseconds(updatedAt),
        )

    fun getAllCampaigns(): List<Campaign> = dbQuery.selectAllCampaigns(::mapCampaignSelecting).executeAsList()

    fun insertCampaign(campaign: Campaign) {
        dbQuery.insertCampaign(
            id = campaign.id,
            name = campaign.name,
            ruleSet = campaign.ruleSet.ordinal.toLong(),
        )
    }

    fun getCampaign(id: String): Campaign? = dbQuery.selectCampaignById(id, ::mapCampaignSelecting).executeAsOneOrNull()

    fun deleteCampaign(id: String) {
        dbQuery.deleteCampaign(id)
    }

    private fun mapCampaignSelecting(id: String, name: String, ruleSet: Long): Campaign =
        Campaign(id = id, name = name, ruleSet = RuleSet.fromInt(ruleSet.toInt()))

    fun getAllUserLists(type: UserList.Type): List<Stored<UserList>> =
        dbQuery.selectAllUserListsByType(type.name, ::mapUserListStored).executeAsList()

    fun getUserList(id: String): UserList? =
        dbQuery.selectUserListById(id, ::mapUserListSelecting).executeAsOneOrNull()

    fun saveUserList(list: UserList, updatedAt: Long) {
        dbQuery.saveUserList(
            id = list.id,
            name = list.name,
            type = list.type.name,
            itemIds = list.itemIds.joinToString(LIST_DELIMITER),
            updatedAt = updatedAt,
        )
    }

    fun deleteUserList(id: String) {
        dbQuery.deleteUserList(id)
    }

    fun initUserPreferences() {
        dbQuery.initUserPreferences()
    }

    fun getUserPreferences(): UserPreferences =
        dbQuery.getUserPreferences { _, theme, palette, distanceUnit ->
            UserPreferences(
                theme = Theme.entries.find { it.name.equals(theme, ignoreCase = true) }
                    ?: Theme.SYSTEM,
                palette = Palette.entries.find { it.name.equals(palette, ignoreCase = true) }
                    ?: Palette.ARCANE,
                distanceUnit = DistanceUnit.entries.find { it.name.equals(distanceUnit, ignoreCase = true) }
                    ?: DistanceUnit.FEET,
            )
        }.executeAsOneOrNull() ?: UserPreferences()

    fun updateTheme(theme: Theme) {
        dbQuery.updateTheme(theme.name.lowercase())
    }

    fun updatePalette(palette: Palette) {
        dbQuery.updatePalette(palette.name.lowercase())
    }

    fun updateDistanceUnit(distanceUnit: DistanceUnit) {
        dbQuery.updateDistanceUnit(distanceUnit.name.lowercase())
    }

    @Suppress("UNUSED_PARAMETER")
    private fun mapUserListSelecting(
        id: String,
        name: String,
        type: String,
        itemIds: String,
        updatedAt: Long,
    ) = UserList(
        id = id,
        name = name,
        type = UserList.Type.valueOf(type),
        itemIds = if (itemIds.isEmpty()) emptyList() else itemIds.split(LIST_DELIMITER),
    )

    private fun mapUserListStored(
        id: String,
        name: String,
        type: String,
        itemIds: String,
        updatedAt: Long,
    ) = Stored(
        value = mapUserListSelecting(id, name, type, itemIds, updatedAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt),
    )

    companion object {
        const val DATABASE_NAME = "ttrpg_companion.db"
        const val LIST_DELIMITER = "\u001F"
    }
}
