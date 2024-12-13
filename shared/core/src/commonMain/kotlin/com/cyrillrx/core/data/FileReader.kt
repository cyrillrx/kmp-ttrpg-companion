package com.cyrillrx.core.data

import com.cyrillrx.core.domain.FileReaderError
import com.cyrillrx.core.domain.Result

interface FileReader {
    suspend fun readFile(path: String): Result<String, FileReaderError>
}
