package com.herologs.feature.privacy

import com.herologs.domain.localdata.DeleteAllLocalDataUseCase
import com.herologs.domain.localdata.GetStoredDataCategoriesUseCase
import com.herologs.domain.localdata.LocalDataCategory
import com.herologs.domain.localdata.LocalDataDeletionResult
import com.herologs.domain.localdata.LocalDataDeletionStage
import com.herologs.domain.localdata.LocalDataRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PrivacyDataViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state exposes the repository inventory`() {
        val repository = FakeLocalDataRepository()

        val state = viewModel(repository).uiState.value

        assertEquals(repository.categories, state.storedCategories)
        assertFalse(state.isDeleteConfirmationVisible)
        assertFalse(state.isDeleting)
    }

    @Test
    fun `delete request can be dismissed without deleting`() {
        val repository = FakeLocalDataRepository()
        val viewModel = viewModel(repository)

        viewModel.requestDeleteAll()
        assertTrue(viewModel.uiState.value.isDeleteConfirmationVisible)

        viewModel.dismissDeleteConfirmation()

        assertFalse(viewModel.uiState.value.isDeleteConfirmationVisible)
        assertEquals(0, repository.deleteCallCount)
    }

    @Test
    fun `confirmation is required before deleting`() = runTest {
        val repository = FakeLocalDataRepository()
        val viewModel = viewModel(repository)

        viewModel.confirmDeleteAll()
        advanceUntilIdle()

        assertEquals(0, repository.deleteCallCount)
    }

    @Test
    fun `confirmed success marks deletion complete`() = runTest {
        val repository = FakeLocalDataRepository()
        val viewModel = viewModel(repository)

        viewModel.requestDeleteAll()
        viewModel.confirmDeleteAll()
        advanceUntilIdle()

        assertEquals(1, repository.deleteCallCount)
        assertFalse(viewModel.uiState.value.isDeleting)
        assertTrue(viewModel.uiState.value.deletionComplete)
    }

    @Test
    fun `failure details remain available to the screen`() = runTest {
        val failure = LocalDataDeletionResult.Failure(
            failedStage = LocalDataDeletionStage.PREFERENCES,
            clearedCategories = setOf(
                LocalDataCategory.TIMELINE_ENTRIES,
                LocalDataCategory.PLACES,
            ),
        )
        val repository = FakeLocalDataRepository(result = failure)
        val viewModel = viewModel(repository)

        viewModel.requestDeleteAll()
        viewModel.confirmDeleteAll()
        advanceUntilIdle()

        assertEquals(failure, viewModel.uiState.value.deletionFailure)
        assertFalse(viewModel.uiState.value.deletionComplete)
    }

    @Test
    fun `retry after partial failure can complete deletion`() = runTest {
        val failure = LocalDataDeletionResult.Failure(
            failedStage = LocalDataDeletionStage.PREFERENCES,
            clearedCategories = setOf(
                LocalDataCategory.TIMELINE_ENTRIES,
                LocalDataCategory.PLACES,
            ),
        )
        val repository = FakeLocalDataRepository(
            results = listOf(failure, LocalDataDeletionResult.Success),
        )
        val viewModel = viewModel(repository)

        viewModel.requestDeleteAll()
        viewModel.confirmDeleteAll()
        advanceUntilIdle()
        assertEquals(failure, viewModel.uiState.value.deletionFailure)

        viewModel.requestDeleteAll()
        assertEquals(null, viewModel.uiState.value.deletionFailure)
        viewModel.confirmDeleteAll()
        advanceUntilIdle()

        assertEquals(2, repository.deleteCallCount)
        assertTrue(viewModel.uiState.value.deletionComplete)
        assertEquals(null, viewModel.uiState.value.deletionFailure)
    }

    @Test
    fun `duplicate confirmations do not start concurrent deletions`() = runTest {
        val pendingResult = CompletableDeferred<LocalDataDeletionResult>()
        val repository = FakeLocalDataRepository(pendingResult = pendingResult)
        val viewModel = viewModel(repository)

        viewModel.requestDeleteAll()
        viewModel.confirmDeleteAll()
        viewModel.confirmDeleteAll()

        assertTrue(viewModel.uiState.value.isDeleting)
        assertEquals(1, repository.deleteCallCount)

        pendingResult.complete(LocalDataDeletionResult.Success)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.deletionComplete)
        assertEquals(1, repository.deleteCallCount)
    }

    private fun viewModel(repository: LocalDataRepository) = PrivacyDataViewModel(
        getStoredDataCategories = GetStoredDataCategoriesUseCase(repository),
        deleteAllLocalData = DeleteAllLocalDataUseCase(repository),
    )

    private class FakeLocalDataRepository(
        val categories: List<LocalDataCategory> = listOf(
            LocalDataCategory.TIMELINE_ENTRIES,
            LocalDataCategory.PLACES,
            LocalDataCategory.APP_PREFERENCES,
        ),
        result: LocalDataDeletionResult = LocalDataDeletionResult.Success,
        private val results: List<LocalDataDeletionResult> = listOf(result),
        private val pendingResult: CompletableDeferred<LocalDataDeletionResult>? = null,
    ) : LocalDataRepository {
        var deleteCallCount = 0
            private set

        override fun getStoredCategories(): List<LocalDataCategory> = categories

        override suspend fun deleteAll(): LocalDataDeletionResult {
            deleteCallCount += 1
            return pendingResult?.await()
                ?: results.getOrElse(deleteCallCount - 1) { results.last() }
        }
    }
}
