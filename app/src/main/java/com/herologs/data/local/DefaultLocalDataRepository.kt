package com.herologs.data.local

import com.herologs.domain.localdata.LocalDataCategory
import com.herologs.domain.localdata.LocalDataDeletionResult
import com.herologs.domain.localdata.LocalDataDeletionStage
import com.herologs.domain.localdata.LocalDataRepository
import kotlinx.coroutines.CancellationException

class DefaultLocalDataRepository(
    private val databaseCleaner: LocalDataCleaner,
    private val preferencesCleaner: LocalDataCleaner,
) : LocalDataRepository {
    override fun getStoredCategories(): List<LocalDataCategory> = STORED_CATEGORIES

    override suspend fun deleteAll(): LocalDataDeletionResult {
        try {
            databaseCleaner.clear()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            return LocalDataDeletionResult.Failure(
                failedStage = LocalDataDeletionStage.DATABASE,
            )
        }

        try {
            preferencesCleaner.clear()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            return LocalDataDeletionResult.Failure(
                failedStage = LocalDataDeletionStage.PREFERENCES,
                clearedCategories = DATABASE_CATEGORIES,
            )
        }

        return LocalDataDeletionResult.Success
    }

    private companion object {
        val DATABASE_CATEGORIES = setOf(
            LocalDataCategory.TIMELINE_ENTRIES,
            LocalDataCategory.PLACES,
        )
        val STORED_CATEGORIES = listOf(
            LocalDataCategory.TIMELINE_ENTRIES,
            LocalDataCategory.PLACES,
            LocalDataCategory.APP_PREFERENCES,
        )
    }
}
