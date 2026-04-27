package com.cyrillrx.core.domain

interface Error

sealed class FileReaderError(val fileName: String, val reason: String?) : Error {
    class Unknown(fileName: String, reason: String? = null) : FileReaderError(fileName, reason)
    class FileNotFound(fileName: String, reason: String? = null) : FileReaderError(fileName, reason)
    class UnableToRead(fileName: String, reason: String? = null) : FileReaderError(fileName, reason)
}
