package com.cyrillrx.rpg.core.data.cache

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.cyrillrx.rpg.cache.AppDatabase
import com.cyrillrx.rpg.core.data.cache.Database.Companion.DATABASE_NAME

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, DATABASE_NAME)
    }
}
