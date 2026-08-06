package com.cyrillrx.rpg

import android.app.Application
import com.cyrillrx.rpg.core.data.cache.AndroidDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

class TtrpgApp : Application() {

    /**
     * Held here rather than in the activity so that an activity recreation reuses the already open
     * connection instead of opening — and leaking — another one.
     */
    val databaseDriverFactory: SharedDatabaseDriverFactory by lazy {
        SharedDatabaseDriverFactory(AndroidDatabaseDriverFactory(this))
    }

    override fun onCreate() {
        super.onCreate()

//        Logger.addChild(LogCat(Severity.VERBOSE, clickableLogs = false))
    }
}
