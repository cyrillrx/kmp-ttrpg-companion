package com.cyrillrx.rpg

import android.content.Context
import com.cyrillrx.logger.Logger
import java.io.IOException

object AssetReader {

    private val TAG = AssetReader::class.java.simpleName

    fun readAsString(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Logger.error(TAG, "Error while reading file '$fileName'", e)
            null
        }
    }
}
