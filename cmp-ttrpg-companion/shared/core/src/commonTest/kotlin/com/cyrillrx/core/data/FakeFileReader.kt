package com.cyrillrx.core.data

import com.cyrillrx.core.domain.FileReaderError
import com.cyrillrx.core.domain.Result

class FakeFileReader(private val json: String) : FileReader {
    override suspend fun readFile(path: String): Result<String, FileReaderError> = Result.Success(json)
}