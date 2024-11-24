package com.cyrillrx.rpg

import android.app.Application
import com.cyrillrx.logger.Logger
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.extension.LogCat

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.addChild(LogCat(Severity.VERBOSE, clickableLogs = false))
    }
}
