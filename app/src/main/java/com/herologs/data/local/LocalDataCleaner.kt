package com.herologs.data.local

/** Clears one local persistence boundary. Implementations must be safe to retry. */
fun interface LocalDataCleaner {
    suspend fun clear()
}
