package com.herologs.domain.localdata

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalDataUseCasesTest {
    @Test
    fun `stored categories are provided by the repository`() {
        val repository = FakeLocalDataRepository(
            categories = listOf(
                LocalDataCategory.TIMELINE_ENTRIES,
                LocalDataCategory.PLACES,
                LocalDataCategory.APP_PREFERENCES,
            ),
        )

        val categories = GetStoredDataCategoriesUseCase(repository)()

        assertEquals(repository.categories, categories)
    }

    @Test
    fun `delete all returns repository success`() = runTest {
        val repository = FakeLocalDataRepository(
            deletionResult = LocalDataDeletionResult.Success,
        )

        val result = DeleteAllLocalDataUseCase(repository)()

        assertEquals(LocalDataDeletionResult.Success, result)
        assertEquals(1, repository.deleteCallCount)
    }

    @Test
    fun `delete all preserves failure details`() = runTest {
        val failure = LocalDataDeletionResult.Failure(
            failedStage = LocalDataDeletionStage.PREFERENCES,
            clearedCategories = setOf(
                LocalDataCategory.TIMELINE_ENTRIES,
                LocalDataCategory.PLACES,
            ),
        )
        val repository = FakeLocalDataRepository(deletionResult = failure)

        val result = DeleteAllLocalDataUseCase(repository)()

        assertEquals(failure, result)
        assertEquals(1, repository.deleteCallCount)
    }

    private class FakeLocalDataRepository(
        val categories: List<LocalDataCategory> = emptyList(),
        private val deletionResult: LocalDataDeletionResult = LocalDataDeletionResult.Success,
    ) : LocalDataRepository {
        var deleteCallCount = 0
            private set

        override fun getStoredCategories(): List<LocalDataCategory> = categories

        override suspend fun deleteAll(): LocalDataDeletionResult {
            deleteCallCount += 1
            return deletionResult
        }
    }
}
