package com.cyrillrx.core.data

interface FileReader {
    suspend fun readFile(path: String): String
}
