package com.herologs.domain.localdata

/** Boundary for inventorying and removing all app-controlled local data. */
interface LocalDataRepository {
    fun getStoredCategories(): List<LocalDataCategory>

    suspend fun deleteAll(): LocalDataDeletionResult
}
