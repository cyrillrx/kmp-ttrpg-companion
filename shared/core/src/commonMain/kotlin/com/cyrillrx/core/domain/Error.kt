package com.cyrillrx.core.domain

sealed interface Error

open class FileReaderError(val fileName: String, val reason: String?) : Error {
    class Unknown(fileName: String, reason: String? = null) : FileReaderError(fileName, reason)
    class FileNotFound(fileName: String, reason: String? = null) : FileReaderError(fileName, reason)
    class UnableToRead(fileName: String, reason: String? = null) : FileReaderError(fileName, reason)
}
