package com.cyrillrx.rpg.core.data

import com.cyrillrx.core.data.FileReader
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rpg_companion.composeapp.generated.resources.Res

class FileReaderImpl : FileReader {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun readFile(path: String): String {
        val bytes = Res.readBytes(path)
        return bytes.decodeToString()
    }
}
