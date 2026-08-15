package com.cyrillrx.rpg

import android.app.Application
import com.cyrillrx.rpg.core.data.cache.AndroidDatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.SharedDatabaseDriverFactory

class TtrpgApp : Application() {

    val databaseDriverFactory: SharedDatabaseDriverFactory by lazy {
        SharedDatabaseDriverFactory(AndroidDatabaseDriverFactory(this))
    }
}
