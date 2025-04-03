package com.cyrillrx.rpg.core.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.domain.FileReaderError
import com.cyrillrx.core.domain.Result
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rpg_companion.composeapp.generated.resources.Res

class ComposeFileReader : FileReader {
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun readFile(path: String): Result<String, FileReaderError> {
        try {
            val bytes = Res.readBytes(path)
            val serialized = bytes.decodeToString()
            return Result.Success(serialized)
        } catch (e: Exception) {
            return Result.Failure(FileReaderError.Unknown(path, e.message))
        }
    }
}
