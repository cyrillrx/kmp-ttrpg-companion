package com.cyrillrx.rpg

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

//        Logger.addChild(LogCat(Severity.VERBOSE, clickableLogs = false))
    }
}
