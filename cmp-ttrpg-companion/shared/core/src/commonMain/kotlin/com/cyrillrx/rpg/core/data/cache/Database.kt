package com.cyrillrx.rpg.core.data.cache

import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.campaign.domain.Campaign
import com.cyrillrx.rpg.campaign.domain.RuleSet
import com.cyrillrx.rpg.userlist.domain.UserList

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

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

    fun getUserList(id: String): UserList? = dbQuery.selectUserListById(id, ::mapUserListSelecting).executeAsOneOrNull()

    fun insertUserList(list: UserList) {
        dbQuery.insertUserList(
            id = list.id,
            name = list.name,
            type = list.type.name,
            itemIds = list.itemIds.joinToString(LIST_DELIMITER),
        )
    }

    fun updateUserList(list: UserList) {
        dbQuery.updateUserList(
            name = list.name,
            itemIds = list.itemIds.joinToString(LIST_DELIMITER),
            id = list.id,
        )
    }

    fun deleteUserList(id: String) {
        dbQuery.deleteUserList(id)
    }

    private fun mapUserListSelecting(id: String, name: String, type: String, itemIds: String): UserList =
        UserList(
            id = id,
            name = name,
            type = UserList.Type.valueOf(type),
            itemIds = if (itemIds.isEmpty()) emptyList() else itemIds.split(LIST_DELIMITER),
        )

    companion object {
        const val DATABASE_NAME = "ttrpg_companion.db"
        const val LIST_DELIMITER = "\u001F"
    }
}
