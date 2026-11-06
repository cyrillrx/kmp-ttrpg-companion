package com.cyrillrx.rpg.app

actual fun currentLocale(): String = java.util.Locale.getDefault().language
